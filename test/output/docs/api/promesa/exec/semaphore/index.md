---
sidebar_label: semaphore
title: promesa.exec.semaphore
toc_min_heading_level: 2
toc_max_heading_level: 4
custom_edit_url:
---

Concurrency limiter: Semaphore




### acquire\! {#acquire-BANG-}
``` clojure
(acquire! sem)
(acquire! sem & {:keys [permits timeout blocking], :or {blocking true, permits 1}})
```


### create {#create}
``` clojure
(create & {:keys [permits], :or {permits 1}})
```


Creates a Semaphore instance.

### release\! {#release-BANG-}
``` clojure
(release! sem)
(release! sem & {:keys [permits]})
```

