
-----
# <a name="promesa.exec.semaphore">promesa.exec.semaphore</a>


Concurrency limiter: Semaphore




## <a name="promesa.exec.semaphore/acquire!">`acquire!`</a><a name="promesa.exec.semaphore/acquire!"></a>
``` clojure

(acquire! sem)
(acquire! sem & {:keys [permits timeout blocking], :or {blocking true, permits 1}})
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec/semaphore.clj#L37-L44">Source</a></sub></p>

## <a name="promesa.exec.semaphore/create">`create`</a><a name="promesa.exec.semaphore/create"></a>
``` clojure

(create & {:keys [permits], :or {permits 1}})
```

Creates a Semaphore instance.
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec/semaphore.clj#L51-L55">Source</a></sub></p>

## <a name="promesa.exec.semaphore/release!">`release!`</a><a name="promesa.exec.semaphore/release!"></a>
``` clojure

(release! sem)
(release! sem & {:keys [permits]})
```
<p><sub><a href="https://github.com/funcool/promesa/blob/master/src/promesa/exec/semaphore.clj#L46-L49">Source</a></sub></p>
