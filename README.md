# Sales Data Management Application

This project is a full-stack application designed to manage, visualize, and analyze sales data. It includes:
- **Backend:** Spring Boot application for managing API endpoints.
- **Frontend:** React application for data visualization and interaction.
- **Database:** PostgreSQL database to store and query sales data.

## Database Schema and Example Data

### Schema Creation
Run the following SQL commands to create the `sales_data` table:

```sql
CREATE TABLE sales_data (
    id SERIAL PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    region VARCHAR(50) NOT NULL,
    product VARCHAR(50) NOT NULL,
    revenue DECIMAL(10, 2) NOT NULL,
    interaction_date TIMESTAMP NOT NULL,
    lead_score DECIMAL(5, 2) NOT NULL
);
```
## Sample Data
```sql
INSERT INTO sales_data (customer_name, region, product, revenue, interaction_date, lead_score)
VALUES
    ('Alice Inc.', 'North America', 'Software', 12000.50, '2024-01-15 09:30:00', 78.5),
    ('Bob Ltd.', 'Europe', 'Hardware', 8000.00, '2024-01-20 14:00:00', 85.2),
    ('Charlie Co.', 'Asia', 'Services', 15000.75, '2024-01-25 11:45:00', 92.3);
```
```markdown
## Backend: Spring Boot Application
The backend is a Spring Boot application that provides RESTful APIs for managing and querying the sales data.
```
### SalesData.java
Represents the sales_data table as a Java entity.
Contains fields such as id, customerName, region, product, revenue, interactionDate, and leadScore.
```java
@Entity
public class SalesData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerName;
    private String region;
    private String product;
    private Double revenue;
    private Timestamp interactionDate;
    private Double leadScore;
    // Getters and setters
}

```

### SalesDataRepository.java
Interface for operations on the sales_data table
```java
public interface SalesDataRepository extends JpaRepository<SalesData, Long> {
  List<SalesData> findByFilters(
              @Param("region") String region,
              @Param("minLeadScore") Double minLeadScore,
              @Param("startDate") LocalDateTime startDate,
              @Param("endDate") LocalDateTime endDate
      );
  List<Object[]> findTotalRevenuePerRegion();
}

```


### SalesDataService.java
```java
public class SalesDataService {

    @Autowired //inject an instance of SalesDataRepository
    private SalesDataRepository repository;

    public List<SalesData> getAllSales() {
        return repository.findAll();
    }

    public List<SalesData> getFilteredSales(String region, Double minLeadScore, LocalDateTime startDate, LocalDateTime endDate) {
        System.out.println("Region: " + region);
        System.out.println("Min Lead Score: " + minLeadScore);
        System.out.println("Start Interaction Date: " + startDate);
        System.out.println("End Interaction Date: " + endDate);

        return repository.findByFilters(region, minLeadScore, startDate, endDate);
    }

    public List<Object[]> getTotalRevenuePerRegion() {
        return repository.findTotalRevenuePerRegion();
    }



}

```

### Controller.java
```java
@RestController
@RequestMapping("/sales")
public class Controller {
    @GetMapping
    public List<SalesData> getAllSales();
    @GetMapping("/filter")
    public List<SalesData> getFilteredSales(@RequestParam String region, @RequestParam Double minLeadScore);
    @GetMapping("/sales/summary")
    public List<String> getTotalRevenuePerRegion();
}
```


## Frontend: React Application
The frontend is a React application built with TypeScript. It provides a user-friendly interface to view, filter, and sort sales data.

### App.tsx
```tsx
import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <div>
        <a href="https://vite.dev" target="_blank">
          <img src={viteLogo} className="logo" alt="Vite logo" />
        </a>
        <a href="https://react.dev" target="_blank">
          <img src={reactLogo} className="logo react" alt="React logo" />
        </a>
      </div>
      <h1>Vite + React</h1>
      <div className="card">
        <button onClick={() => setCount((count) => count + 1)}>
          count is {count}
        </button>
        <p>
          Edit <code>src/App.tsx</code> and save to test HMR
        </p>
      </div>
      <p className="read-the-docs">
        Click on the Vite and React logos to learn more
      </p>
    </>
  )
}

export default App


```

### SalesDashboard.tsx
```tsx
import React, { useEffect, useState } from 'react';
import axios from 'axios';

interface Sale {
    id: number;
    customerName: string;
    region: string;
    product: string;
    revenue: number;
}

const SalesDashboard: React.FC = () => {
    const [sales, setSales] = useState<Sale[]>([]);
    const [sortColumn, setSortColumn] = useState<string>('customerName');
    const [sortOrder, setSortOrder] = useState<'asc' | 'desc'>('asc');

    useEffect(() => {
        axios.get('http://localhost:8080/sales')
            .then(response => setSales(response.data))
            .catch(error => console.error('Error fetching sales data:', error));
    }, []);

    const handleSort = (column: keyof Sale) => {
        const newSortOrder = sortColumn === column && sortOrder === 'asc' ? 'desc' : 'asc';
        setSortColumn(column);
        setSortOrder(newSortOrder);

        const sortedData = [...sales].sort((a, b) => {
            if (column === 'revenue') {
                return newSortOrder === 'asc'
                    ? a[column] - b[column]
                    : b[column] - a[column];
            }
            const valA = a[column].toString().toLowerCase();
            const valB = b[column].toString().toLowerCase();
            return newSortOrder === 'asc'
                ? valA.localeCompare(valB)
                : valB.localeCompare(valA);
        });

        setSales(sortedData);
    };

    return (
        <div
            style={{
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'center',
                alignItems: 'center',
                height: '100vh',
                textAlign: 'center',
                backgroundColor: '#f8f9fa', // Light background
                fontFamily: 'Arial, sans-serif',
            }}
        >
            <h1 style={{ marginBottom: '20px', fontSize: '2.5rem', color: '#333' }}>
                Sales Dashboard
            </h1>
            <div
                style={{
                    border: '1px solid #ccc',
                    padding: '20px',
                    height: '500px',
                    width: '80%',
                    maxWidth: '800px',
                    backgroundColor: 'darkslategray',
                    overflowY: 'scroll',
                    boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
                    borderRadius: '10px',
                }}
            >
                <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                    <thead>
                    <tr>
                        <th
                            style={{ cursor: 'pointer', padding: '10px', textAlign: 'left', borderBottom: '1px solid #ddd' }}
                            onClick={() => handleSort('customerName')}
                        >
                            Customer {sortColumn === 'customerName' && (sortOrder === 'asc' ? '↑' : '↓')}
                        </th>
                        <th
                            style={{ cursor: 'pointer', padding: '10px', textAlign: 'left', borderBottom: '1px solid #ddd' }}
                            onClick={() => handleSort('region')}
                        >
                            Region {sortColumn === 'region' && (sortOrder === 'asc' ? '↑' : '↓')}
                        </th>
                        <th
                            style={{ cursor: 'pointer', padding: '10px', textAlign: 'left', borderBottom: '1px solid #ddd' }}
                            onClick={() => handleSort('product')}
                        >
                            Product {sortColumn === 'product' && (sortOrder === 'asc' ? '↑' : '↓')}
                        </th>
                        <th
                            style={{ cursor: 'pointer', padding: '10px', textAlign: 'left', borderBottom: '1px solid #ddd' }}
                            onClick={() => handleSort('revenue')}
                        >
                            Revenue {sortColumn === 'revenue' && (sortOrder === 'asc' ? '↑' : '↓')}
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    {sales.map((sale) => (
                        <tr key={sale.id} style={{ borderBottom: '1px solid #eee' }}>
                            <td style={{ padding: '10px', textAlign: 'left' }}>{sale.customerName}</td>
                            <td style={{ padding: '10px', textAlign: 'left' }}>{sale.region}</td>
                            <td style={{ padding: '10px', textAlign: 'left' }}>{sale.product}</td>
                            <td style={{ padding: '10px', textAlign: 'left' }}>${sale.revenue.toFixed(2)}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default SalesDashboard;


```


## How to Run the Application

### Setup the Database
- Import the `sales_data_backup.sql` file into your PostgreSQL instance.

### Run the Backend
- Navigate to the Spring Boot project folder.
- Execute:
```bash
mvn spring-boot:run
```

### Run the Frontend
- Navigate to the React project folder.
- Execute:
```bash
npm install
npm start
```


## Features
- **Backend:** Scalable, API-driven architecture with endpoints for data retrieval and filtering.
- **Frontend:** Interactive React dashboard for sorting and visualizing sales data.
- **Database:** PostgreSQL schema with sample sales data for testing.
