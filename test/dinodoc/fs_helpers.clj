(ns dinodoc.fs-helpers
  (:require
   [babashka.fs :as fs]
   [clojure.string :as str]))

(defn relfs [dir]
  {:dir dir
   :fspit (fn [filename content & opts]
            (let [path (str dir "/" filename)]
              (fs/create-dirs (fs/parent path))
              (apply spit path content opts)))
   :fslurp (fn [filename & opts]
             (apply slurp (str dir "/" filename) opts))})

(defn with-temp-dir [f]
  (fs/with-temp-dir [dir {}]
    (f (relfs dir))))

(defn fsdata [dir]
  (let [file (fs/file dir)
        path (str file "/")]
    (->> (file-seq file)
         (next) ;; first item is the directory itself, drop it
         (reduce (fn [m filename]
                   (let [filepath (-> (str filename)
                                      (str/replace-first path ""))
                         segments (str/split filepath #"/")]
                     (cond
                       (fs/regular-file? filename) (assoc-in m segments (slurp filename))
                       (fs/directory? filename) (update-in m segments #(or % {}))
                       :else (throw (ex-info "Unsupported file type" {:filename filename})))))
                 {}))))

(comment
  (let [{:keys [dir fspit fslurp]} (relfs "tmp")]
    (fspit "file.txt" "foo")
    (fspit "nested/file.txt" "bar")
    (fsdata dir)))
