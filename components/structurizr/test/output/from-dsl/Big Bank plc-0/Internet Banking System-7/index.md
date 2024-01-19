# Internet Banking System

Allows customers to view information about their bank accounts, and make payments.

Containers:
- [API Application](./API%20Application-11/)
- [Database](./Database-18/)
- [Mobile App](./Mobile%20App-9/)
- [Single-Page Application](./Single-Page%20Application-8/)
- [Web Application](./Web%20Application-10/)
### SystemContext

```mermaid
graph TB
  linkStyle default fill:transparent

  subgraph diagram [Internet Banking System - System Context]
    style diagram fill:transparent,stroke:#ffffff

    subgraph group1 [Big Bank plc]
      style group1 fill:transparent,stroke:#cccccc,color:#cccccc,stroke-dasharray:5

      4["<div style='font-weight: bold'>Mainframe Banking System</div><div style='font-size: 70%; margin-top: 0px'>[Software System]</div><div style='font-size: 80%; margin-top:10px'>Stores all of the core<br />banking information about<br />customers, accounts,<br />transactions, etc.</div>"]
      click 4 "/Big%20Bank%20plc-0/Mainframe%20Banking%20System-4" "/Big%20Bank%20plc-0/Mainframe%20Banking%20System-4"
      style 4 fill:#999999,stroke:#6b6b6b,color:#ffffff
      5["<div style='font-weight: bold'>E-mail System</div><div style='font-size: 70%; margin-top: 0px'>[Software System]</div><div style='font-size: 80%; margin-top:10px'>The internal Microsoft<br />Exchange e-mail system.</div>"]
      click 5 "/Big%20Bank%20plc-0/E-mail%20System-5" "/Big%20Bank%20plc-0/E-mail%20System-5"
      style 5 fill:#999999,stroke:#6b6b6b,color:#ffffff
      7["<div style='font-weight: bold'>Internet Banking System</div><div style='font-size: 70%; margin-top: 0px'>[Software System]</div><div style='font-size: 80%; margin-top:10px'>Allows customers to view<br />information about their bank<br />accounts, and make payments.</div>"]
      click 7 "/Big%20Bank%20plc-0/Internet%20Banking%20System-7" "/Big%20Bank%20plc-0/Internet%20Banking%20System-7"
      style 7 fill:#1168bd,stroke:#0b4884,color:#ffffff
    end

    1["<div style='font-weight: bold'>Personal Banking Customer</div><div style='font-size: 70%; margin-top: 0px'>[Person]</div><div style='font-size: 80%; margin-top:10px'>A customer of the bank, with<br />personal bank accounts.</div>"]
    style 1 fill:#08427b,stroke:#052e56,color:#ffffff

    1-. "<div>Views account balances, and<br />makes payments using</div><div style='font-size: 70%'></div>" .->7
    7-. "<div>Gets account information<br />from, and makes payments<br />using</div><div style='font-size: 70%'></div>" .->4
    7-. "<div>Sends e-mail using</div><div style='font-size: 70%'></div>" .->5
    5-. "<div>Sends e-mails to</div><div style='font-size: 70%'></div>" .->1
  end
```

## Deployment - Development
### DevelopmentDeployment

```mermaid
graph TB
  linkStyle default fill:transparent

  subgraph diagram [Internet Banking System - Deployment - Development]
    style diagram fill:transparent,stroke:#ffffff

    subgraph 50 [Developer Laptop]
      style 50 fill:transparent,stroke:#888888,color:#000000

      subgraph 51 [Web Browser]
        style 51 fill:transparent,stroke:#888888,color:#000000

        52["<div style='font-weight: bold'>Single-Page Application</div><div style='font-size: 70%; margin-top: 0px'>[Container: JavaScript and Angular]</div><div style='font-size: 80%; margin-top:10px'>Provides all of the Internet<br />banking functionality to<br />customers via their web<br />browser.</div>"]
        style 52 fill:#438dd5,stroke:#2e6295,color:#ffffff
      end

      subgraph 53 [Docker Container - Web Server]
        style 53 fill:transparent,stroke:#888888,color:#000000

        subgraph 54 [Apache Tomcat]
          style 54 fill:transparent,stroke:#888888,color:#000000

          55["<div style='font-weight: bold'>Web Application</div><div style='font-size: 70%; margin-top: 0px'>[Container: Java and Spring MVC]</div><div style='font-size: 80%; margin-top:10px'>Delivers the static content<br />and the Internet banking<br />single page application.</div>"]
          style 55 fill:#438dd5,stroke:#2e6295,color:#ffffff
          57["<div style='font-weight: bold'>API Application</div><div style='font-size: 70%; margin-top: 0px'>[Container: Java and Spring MVC]</div><div style='font-size: 80%; margin-top:10px'>Provides Internet banking<br />functionality via a<br />JSON/HTTPS API.</div>"]
          style 57 fill:#438dd5,stroke:#2e6295,color:#ffffff
        end

      end

      subgraph 59 [Docker Container - Database Server]
        style 59 fill:transparent,stroke:#888888,color:#000000

        subgraph 60 [Database Server]
          style 60 fill:transparent,stroke:#888888,color:#000000

          61[("<div style='font-weight: bold'>Database</div><div style='font-size: 70%; margin-top: 0px'>[Container: Oracle Database Schema]</div><div style='font-size: 80%; margin-top:10px'>Stores user registration<br />information, hashed<br />authentication credentials,<br />access logs, etc.</div>")]
          style 61 fill:#438dd5,stroke:#2e6295,color:#ffffff
        end

      end

    end

    subgraph 63 [Big Bank plc]
      style 63 fill:transparent,stroke:#888888,color:#000000

      subgraph 64 [bigbank-dev001]
        style 64 fill:transparent,stroke:#888888,color:#000000

        65["<div style='font-weight: bold'>Mainframe Banking System</div><div style='font-size: 70%; margin-top: 0px'>[Software System]</div><div style='font-size: 80%; margin-top:10px'>Stores all of the core<br />banking information about<br />customers, accounts,<br />transactions, etc.</div>"]
        style 65 fill:#999999,stroke:#6b6b6b,color:#ffffff
      end

    end

    55-. "<div>Delivers to the customer's<br />web browser</div><div style='font-size: 70%'></div>" .->52
    52-. "<div>Makes API calls to</div><div style='font-size: 70%'>[JSON/HTTPS]</div>" .->57
    57-. "<div>Reads from and writes to</div><div style='font-size: 70%'>[SQL/TCP]</div>" .->61
    57-. "<div>Makes API calls to</div><div style='font-size: 70%'>[XML/HTTPS]</div>" .->65
  end
```

## Deployment - Live
### LiveDeployment

```mermaid
graph TB
  linkStyle default fill:transparent

  subgraph diagram [Internet Banking System - Deployment - Live]
    style diagram fill:transparent,stroke:#ffffff

    subgraph 67 [Customer's mobile device]
      style 67 fill:transparent,stroke:#888888,color:#000000

      68["<div style='font-weight: bold'>Mobile App</div><div style='font-size: 70%; margin-top: 0px'>[Container: Xamarin]</div><div style='font-size: 80%; margin-top:10px'>Provides a limited subset of<br />the Internet banking<br />functionality to customers<br />via their mobile device.</div>"]
      style 68 fill:#438dd5,stroke:#2e6295,color:#ffffff
    end

    subgraph 69 [Customer's computer]
      style 69 fill:transparent,stroke:#888888,color:#000000

      subgraph 70 [Web Browser]
        style 70 fill:transparent,stroke:#888888,color:#000000

        71["<div style='font-weight: bold'>Single-Page Application</div><div style='font-size: 70%; margin-top: 0px'>[Container: JavaScript and Angular]</div><div style='font-size: 80%; margin-top:10px'>Provides all of the Internet<br />banking functionality to<br />customers via their web<br />browser.</div>"]
        style 71 fill:#438dd5,stroke:#2e6295,color:#ffffff
      end

    end

    subgraph 72 [Big Bank plc]
      style 72 fill:transparent,stroke:#888888,color:#000000

      subgraph 73 [bigbank-web***]
        style 73 fill:transparent,stroke:#888888,color:#000000

        subgraph 74 [Apache Tomcat]
          style 74 fill:transparent,stroke:#888888,color:#000000

          75["<div style='font-weight: bold'>Web Application</div><div style='font-size: 70%; margin-top: 0px'>[Container: Java and Spring MVC]</div><div style='font-size: 80%; margin-top:10px'>Delivers the static content<br />and the Internet banking<br />single page application.</div>"]
          style 75 fill:#438dd5,stroke:#2e6295,color:#ffffff
        end

      end

      subgraph 77 [bigbank-api***]
        style 77 fill:transparent,stroke:#888888,color:#000000

        subgraph 78 [Apache Tomcat]
          style 78 fill:transparent,stroke:#888888,color:#000000

          79["<div style='font-weight: bold'>API Application</div><div style='font-size: 70%; margin-top: 0px'>[Container: Java and Spring MVC]</div><div style='font-size: 80%; margin-top:10px'>Provides Internet banking<br />functionality via a<br />JSON/HTTPS API.</div>"]
          style 79 fill:#438dd5,stroke:#2e6295,color:#ffffff
        end

      end

      subgraph 82 [bigbank-db01]
        style 82 fill:transparent,stroke:#888888,color:#000000

        subgraph 83 [Oracle - Primary]
          style 83 fill:transparent,stroke:#888888,color:#000000

          84[("<div style='font-weight: bold'>Database</div><div style='font-size: 70%; margin-top: 0px'>[Container: Oracle Database Schema]</div><div style='font-size: 80%; margin-top:10px'>Stores user registration<br />information, hashed<br />authentication credentials,<br />access logs, etc.</div>")]
          style 84 fill:#438dd5,stroke:#2e6295,color:#ffffff
        end

      end

      subgraph 86 [bigbank-db02]
        style 86 fill:transparent,stroke:#888888,color:#000000

        subgraph 87 [Oracle - Secondary]
          style 87 fill:transparent,stroke:#888888,color:#000000

          88[("<div style='font-weight: bold'>Database</div><div style='font-size: 70%; margin-top: 0px'>[Container: Oracle Database Schema]</div><div style='font-size: 80%; margin-top:10px'>Stores user registration<br />information, hashed<br />authentication credentials,<br />access logs, etc.</div>")]
          style 88 fill:#438dd5,stroke:#2e6295,color:#ffffff
        end

      end

      subgraph 90 [bigbank-prod001]
        style 90 fill:transparent,stroke:#888888,color:#000000

        91["<div style='font-weight: bold'>Mainframe Banking System</div><div style='font-size: 70%; margin-top: 0px'>[Software System]</div><div style='font-size: 80%; margin-top:10px'>Stores all of the core<br />banking information about<br />customers, accounts,<br />transactions, etc.</div>"]
        style 91 fill:#999999,stroke:#6b6b6b,color:#ffffff
      end

    end

    75-. "<div>Delivers to the customer's<br />web browser</div><div style='font-size: 70%'></div>" .->71
    71-. "<div>Makes API calls to</div><div style='font-size: 70%'>[JSON/HTTPS]</div>" .->79
    68-. "<div>Makes API calls to</div><div style='font-size: 70%'>[JSON/HTTPS]</div>" .->79
    79-. "<div>Reads from and writes to</div><div style='font-size: 70%'>[SQL/TCP]</div>" .->84
    79-. "<div>Reads from and writes to</div><div style='font-size: 70%'>[SQL/TCP]</div>" .->88
    79-. "<div>Makes API calls to</div><div style='font-size: 70%'>[XML/HTTPS]</div>" .->91
  end
```

