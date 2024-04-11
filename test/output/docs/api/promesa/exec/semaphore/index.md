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


[source](/blob/master/test/projects/promesa/src/promesa/exec/semaphore.clj#L37-L44)


### create {#create}
``` clojure
(create & {:keys [permits], :or {permits 1}})
```


Creates a Semaphore instance.

[source](/blob/master/test/projects/promesa/src/promesa/exec/semaphore.clj#L51-L55)


### release\! {#release-BANG-}
``` clojure
(release! sem)
(release! sem & {:keys [permits]})
```


[source](/blob/master/test/projects/promesa/src/promesa/exec/semaphore.clj#L46-L49)

