# Big Bank plc

This is an example workspace to illustrate the key features of Structurizr, via the DSL, based around a fictional online banking system.

Systems:

- [ATM](./ATM-6/)
- [E-mail System](./E-mail%20System-5/)
- [Internet Banking System](./Internet%20Banking%20System-7/)
- [Mainframe Banking System](./Mainframe%20Banking%20System-4/)


### SystemLandscape

```mermaid
graph TB
  linkStyle default fill:transparent

  subgraph diagram [System Landscape]
    style diagram fill:transparent,stroke:#ffffff

    subgraph enterprise [Big Bank plc]
      style enterprise fill:transparent,stroke:#444444,color:#444444

      2["<div style='font-weight: bold'>Customer Service Staff</div><div style='font-size: 70%; margin-top: 0px'>[Person]</div><div style='font-size: 80%; margin-top:10px'>Customer service staff within<br />the bank.</div>"]
      style 2 fill:#999999,stroke:#6b6b6b,color:#ffffff
      3["<div style='font-weight: bold'>Back Office Staff</div><div style='font-size: 70%; margin-top: 0px'>[Person]</div><div style='font-size: 80%; margin-top:10px'>Administration and support<br />staff within the bank.</div>"]
      style 3 fill:#999999,stroke:#6b6b6b,color:#ffffff
      4["<div style='font-weight: bold'>Mainframe Banking System</div><div style='font-size: 70%; margin-top: 0px'>[Software System]</div><div style='font-size: 80%; margin-top:10px'>Stores all of the core<br />banking information about<br />customers, accounts,<br />transactions, etc.</div>"]
      click 4 "/Big%20Bank%20plc-0/Mainframe%20Banking%20System-4" "/Big%20Bank%20plc-0/Mainframe%20Banking%20System-4"
      style 4 fill:#999999,stroke:#6b6b6b,color:#ffffff
      5["<div style='font-weight: bold'>E-mail System</div><div style='font-size: 70%; margin-top: 0px'>[Software System]</div><div style='font-size: 80%; margin-top:10px'>The internal Microsoft<br />Exchange e-mail system.</div>"]
      click 5 "/Big%20Bank%20plc-0/E-mail%20System-5" "/Big%20Bank%20plc-0/E-mail%20System-5"
      style 5 fill:#999999,stroke:#6b6b6b,color:#ffffff
      6["<div style='font-weight: bold'>ATM</div><div style='font-size: 70%; margin-top: 0px'>[Software System]</div><div style='font-size: 80%; margin-top:10px'>Allows customers to withdraw<br />cash.</div>"]
      click 6 "/Big%20Bank%20plc-0/ATM-6" "/Big%20Bank%20plc-0/ATM-6"
      style 6 fill:#999999,stroke:#6b6b6b,color:#ffffff
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
    1-. "<div>Asks questions to</div><div style='font-size: 70%'>[Telephone]</div>" .->2
    2-. "<div>Uses</div><div style='font-size: 70%'></div>" .->4
    1-. "<div>Withdraws cash using</div><div style='font-size: 70%'></div>" .->6
    6-. "<div>Uses</div><div style='font-size: 70%'></div>" .->4
    3-. "<div>Uses</div><div style='font-size: 70%'></div>" .->4
  end
```

