# iyzico coding challenge

Improvements were made within the scope of Iyzico challenge.

## Rest Services

#### Product
| Route | HTTP Verb	 | POST body	 | Description	 |
| --- | --- | --- | --- |
| /api/v1/product | `GET` | Empty | List all products. |
| /api/v1/product | `POST` | {"name": "Product Name","description": "Product Description","price": "25", stock": 5} | Create a new product. |
| /api/v1/product/:productId | `GET` | Empty | Get a product. |
| /api/v1/product/:productId | `PUT` | {"name": "Product Name Update","description": "Product Description Update","price": "50", stock": 50} | Update a product with new info. |
| /api/v1/product/:productId | `DELETE` | Empty | Delete a product. |


#### Payment

| Route | HTTP Verb	 | POST body	 | Description	 |
| --- | --- | --- | --- |
| /api/v1/payment | `POST` | { "productId": 1,"quantity": 5 } | Buy a product. |

## Info

* HSQLDB was run in server mode.
(If not HSQLDB on your local machine hsqldb 2.4.0 can be downloaded from https://sourceforge.net/projects/hsqldb/files/hsqldb/hsqldb_2_4/hsqldb-2.4.0.zip/download)

* The DB was started by running the command:
```sh
java -classpath lib/hsqldb.jar org.hsqldb.server.Server --database.0 file:hsqldb/db --dbname.0 db" while in the directory of the hsqldb 2.4.0 file.
```

![hsqldb](https://user-images.githubusercontent.com/47754791/130368012-fbe71045-6512-4df4-be49-566478cb1cb4.PNG)

* The datasource information in the application.yml files of the application has been updated with the DB information I have run.

* Test coverage is more than 80% in Class, Method, and Line coverage.

###### For this case:
* Customers paying for the same product at the same time should not buy the product if the stock is depleted. (i.e. if there are 2 stocks left and 3 customers pay at the same time, first 2 successful should buy the product and the 3rd one should fail with an appropriate message.)

The explanation of the operation made in the PaymentServiceImpl class is given:
> /*
        While the stock information is being updated,
        the synchronized keyword is used in the method that performs this operation.
        When the Synchronized keyword is placed at the beginning of the method,
        the threads that want to access the method enter the method in order,
        and the other thread cannot enter before one thread finishes the method.
        In this way, customers who pay for the same product at the same time are prevented
        from purchasing the product if the stock runs out.
     */

###### For this case:
* In the simulation for some reason the bank response times take ~5 seconds. Due to this latency, a database connection problem is encountered after some time. (Running the IyzicoPaymentServiceTest.java class displays "Connection is not available, request timed out after 30005ms." error after some time.)
Find a way to persist bank responses to the database in this situation.

An explanation of the operation made to the IyzicoPaymentServiceTest class has been made. It can be checked by running the test:
 > /*
        The ThreadPoolConfiguration file I created under the
        Configuration package was customized using the ThreadPoolTaskExecutor class.
        By setting the CorePoolSize value to 2,
        2 threads are running in parallel,
        and the db pool size was set to 2 by default,
        without causing an error in our database,
        and a successful result was obtained despite a waiting time of 5 seconds.
     */


## Challenge Details

As part of our interview process, we expect you to complete a coding challenge in order for us to better understand your coding skills. The challenge is a Java8 + Spring Boot project which uses HSQLDB as the database.


# Question 1: Product Management

Most of iyzico's merchants sell products online. For these merchants the necessary REST services are listed below. We kindly ask you to implement them.

## Requirements

* Product adding, removing or updating services. 
* Product listing service which returns product name, description, remaining stock count and price.
* Payment service for the end user to buy their selected product.
* A product should not be sold for more than its stock.
* Customers paying for the same product at the same time should not buy the product if the stock is depleted. (i.e. if there are 2 stocks left and 3 customers pay at the same time, first 2 successful should buy the product and the 3rd one should fail with an appropriate message.) 
* No front end is necessary.
* Test coverage for the implemented service should be above 80%. We expect both Integration and unit tests.
* Bonus: Iyzico payment integration can be implemented for payment step. 
Reference: [https://dev.iyzipay.com/](https://dev.iyzipay.com/)


# Question 2 : Latency Management

Iyzico provides its payment service by calling bank endpoints. The bank responses are persisted to database.In [IyzicoPaymentServiceTest.java](src/test/java/com/iyzico/challenge/service/IyzicoPaymentServiceTest.java)
class we have simulated 100 customers calling the payment service.

```java
    public void pay(BigDecimal price) {
        //pay with bank
        BankPaymentRequest request = new BankPaymentRequest();
        request.setPrice(price);
        BankPaymentResponse response = bankService.pay(request);

        //insert records
        Payment payment = new Payment();
        payment.setBankResponse(response.getResultCode());
        payment.setPrice(price);
        paymentRepository.save(payment);
        logger.info("Payment saved successfully!");
    }
```

In the simulation for some reason the bank response times take ~5 seconds. Due to this latency, a database connection problem is encountered after some time. (Running the [IyzicoPaymentServiceTest.java](src/test/java/com/iyzico/challenge/service/IyzicoPaymentServiceTest.java)
class displays "Connection is not available, request timed out after 30005ms." error after some time.)

Find a way to persist bank responses to the database in this situation.

## Requirements

* DB connection pool should stay the same.
* BankService.java, PaymentServiceClients.java and IyzicoPaymentServiceTest.java classes should not be changed.
* In case of an error, there should not be any dirty data in the database.






