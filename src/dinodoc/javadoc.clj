(ns dinodoc.javadoc
  (:require
   [babashka.fs :as fs]
   [babashka.process :refer [shell]]
   [clojure.string :as str]
   [dinodoc.generator :as generator]
   [dinodoc.impl.fs :refer [create-local-temp-dir]]
   [hickory.core :as h]
   [hickory.select :as hs]))

(defn- segments->path [segments]
  (str (str/join "/" segments)
       ".html"))

(defn- file->id-map [file]
  (let [tree (-> (slurp file)
                 h/parse h/as-hickory)]
    (->> (hs/select (hs/attr :id) tree)
         (map (comp :id :attrs))
         (keep (fn [html-id]
                 (when-not (str/includes? html-id "-")
                   (if-some [[match id] (re-matches #"(.*)\(.*\)" html-id)]
                     [id match]
                     [html-id html-id]))))
         (into {}))))

(defn resolve-link [javadoc-path definition]
  (let [segments (str/split definition #"\.")
        target (segments->path segments)]
    (if (fs/exists? (fs/file javadoc-path target))
      target
      (let [target (segments->path (butlast segments))
            target-file (fs/file javadoc-path target)]
        (when (fs/exists? target-file)
          ;; Possible optimization: could cache/memoize by file
          (let [id-map (file->id-map target-file)]
            (when-some [anchor (get id-map (last segments))]
              (str target "#" anchor))))))))

(comment
  (def tree (-> (h/parse "<a href=\"foo\">foo</a>")
                (h/as-hickory)))

  (def tree (-> (slurp "examples/javadoc/static/examples/javadoc/api/demo/Greeter.html")
                h/parse h/as-hickory))

  (->> (hs/select (hs/attr :id) tree)
       (map (fn [{:keys [tag attrs]}]
              [tag (:id attrs)])))

  (->> (hs/select (hs/attr :id) tree)
       (map (comp :id :attrs))
       (keep (fn [html-id]
               (when-not (str/includes? html-id "-")
                 (if-some [[match id] (re-matches #"(.*)\(.*\)" html-id)]
                   [id match]
                   [html-id html-id]))))
       (into {})))

(deftype ^:private JavadocGenerator [opts tmp-dir]
  generator/Generator
  (prepare-index [_]
    (let [{:keys [sourcepath subpackages]} opts]
      (shell "javadoc -sourcepath" sourcepath "-subpackages" subpackages "-d" tmp-dir)))
  (resolve-link [_ target]
    (resolve-link tmp-dir target))
  (generate [_ {:keys [output-path]}]
    (fs/move tmp-dir output-path)))

(defn make-generator [opts]
  (->JavadocGenerator opts (str (create-local-temp-dir))))
