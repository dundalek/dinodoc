---
sidebar_label: protocols
title: samples.protocols
toc_min_heading_level: 2
toc_max_heading_level: 4
custom_edit_url:
---





### IBulkhead {#IBulkhead}


Bulkhead main API

*protocol*


#### \-get\-stats {#-get-stats}
``` clojure
(-get-stats _)
```


Get internal statistics of the bulkhead instance

*protocol*


#### \-invoke\! {#-invoke-BANG-}
``` clojure
(-invoke! _ f)
```


Call synchronously a function under bulkhead context

*protocol*


### IQueue {#IQueue}


*protocol*


#### \-offer\! {#-offer-BANG-}
``` clojure
(-offer! _ _)
```


*protocol*


#### \-poll\! {#-poll-BANG-}
``` clojure
(-poll! _)
```


*protocol*

