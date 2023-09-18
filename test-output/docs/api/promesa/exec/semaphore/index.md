---
sidebar_label: semaphore
title: promesa.exec.semaphore
toc_min_heading_level: 2
toc_max_heading_level: 4
---

Concurrency limiter: Semaphore




### acquire\! {#acquire-BANG-}
``` clojure
(acquire! sem)
(acquire! sem & {:keys [permits timeout blocking], :or {blocking true, permits 1}})
```

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec/semaphore.clj#L37-L44">Source</a></sub></p>

### create {#create}
``` clojure
(create & {:keys [permits], :or {permits 1}})
```


Creates a Semaphore instance.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec/semaphore.clj#L51-L55">Source</a></sub></p>

### release\! {#release-BANG-}
``` clojure
(release! sem)
(release! sem & {:keys [permits]})
```

<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec/semaphore.clj#L46-L49">Source</a></sub></p>
