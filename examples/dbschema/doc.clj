(ns doc
  (:require
   [babashka.fs :as fs]
   [babashka.process :refer [shell]]
   [dinodoc.api :as dinodoc]
   [dinodoc.impl.tbls :as tbls]))

(defn with-temporary-postgres-db [f]
  (fs/with-temp-dir [dir {}]
    (let [data-dir (str dir "/data")
          db-name "mydb"
          uri (str "postgresql://" dir "/" db-name)]
      (shell "initdb -D" data-dir)
      (shell "pg_ctl start -D" data-dir "-o" "-h ''" "-o" "-k" "-o" dir)
      (shell "createdb -h" dir db-name)
      (try
        (f {:host dir :db db-name :uri uri})
        (finally
          (shell "pg_ctl stop -D" data-dir))))))

(with-temporary-postgres-db
  (fn [{:keys [host db uri]}]
    (shell "psql --quiet -f" "pagila/pagila-schema.sql"
           "-h" host "-d" db)

    (dinodoc/generate
     {:inputs [{:path "."}
               {:generator (tbls/make-generator
                            {:dsn (str "sqlite:" (fs/absolutize "./chinook/ChinookDatabase/DataSources/Chinook_Sqlite.sqlite"))})
                :output-path "chinook"}
               {:generator (tbls/make-generator
                            {:dsn uri
                             :title "Sakila"})
                :output-path "sakila"}]
      :output-path "docs"})))

(shutdown-agents)
