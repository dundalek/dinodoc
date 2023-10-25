(ns dinodoc.impl.git
  {:no-doc true}
  (:require
   [clojure.java.shell :refer [sh]]
   [clojure.string :as str]))

(defn current-branch [repo-path]
  (let [cmd ["git" "branch" "--show-current" :dir repo-path]
        {:keys [exit out] :as result} (apply sh cmd)]
    (when (not= exit 0)
      (throw (ex-info "Failed to determine current branch"
                      {:result result
                       :cmd cmd})))
    (str/trim out)))

(defn branch-remote [repo-path branch]
  (let [cmd ["git" "config" (str "branch." branch ".remote") :dir repo-path]
        {:keys [exit out] :as result} (apply sh cmd)]
    (when (not= exit 0)
      (throw (ex-info "Failed to determine branch remote"
                      {:result result
                       :cmd cmd})))
    (str/trim out)))

(defn remote-url [repo-path remote]
  (let [cmd ["git" "remote" "-v" :dir repo-path]
        {:keys [exit out] :as result} (apply sh cmd)]
    (when (not= exit 0)
      (throw (ex-info "Failed to determine remote url"
                      {:result result
                       :cmd cmd})))
    (->> (str/split-lines out)
         (some (fn [line]
                 (let [[item-remote url] (str/split line #"\s")]
                   ;; third match on line is "(fetch)" or "(pull)" but we don't distinguish between these and return first match
                   (when (= remote item-remote)
                     url)))))))

(defn remote-url-to-web [url]
  (-> (if-some [[_ host path] (re-matches #"git@([^:]+):(.+)" url)]
        (str "https://" host "/" path)
        url)
      (str/replace #"\.git$" "")))

(defn detect-repo-info [repo-path]
  (try
    (let [branch (current-branch repo-path)
          remote (branch-remote repo-path branch)
          url (-> (remote-url repo-path remote)
                  (remote-url-to-web))]
      {:url url
       :branch branch})
    (catch Exception _e
      ;; in debug level could log error
      nil)))

(comment
  (current-branch ".")

  (branch-remote "." "main")

  (remote-url "." "origin")

  (detect-repo-info "."))
