(defproject com.sagevisuals/trlisp "0-SNAPSHOT1"
  :description "A Lisp-y implementation of B. Jay's unlabelled binary tree
 calculus."
  :url "https://github.com/blosavio/trlisp"
  :license {:name "MIT License"
            :url "https://opensource.org/license/mit"
            :distribution :repo}
  :dependencies [[org.clojure/clojure "1.12.0"]
                 [org.clojure/test.check "1.1.1"]
                 [com.sagevisuals/thingy "1"]]
  :repl-options {:init-ns trlisp.core}
  :profiles {:dev {:dependencies [[com.sagevisuals/chlog "1"]
                                  [com.sagevisuals/readmoi "3"]
                                  [com.sagevisuals/screedcast "2-SNAPSHOT1"]]
                   :plugins [[dev.weavejester/lein-cljfmt "0.12.0"]
                             [lein-codox "0.10.8"]]}
             :repl {}}
  :codox {:metadata {:doc/format :markdown}
          :namespaces [#"^tree-calculus"]
          :target-path "doc"
          :output-path "doc"
          :doc-files []
          :source-uri "https://github.com/blosavio/trlisp/blob/main/{filepath}#L{line}"
          :html {:transforms [[:div.sidebar.primary] [:append [:ul.index-link [:li.depth-1 [:a {:href "https://github.com/blosavio/trlisp"} "Project home"]]]]]}
          :project {:name "trlisp" :version "version 0"}}
  :scm {:name "git" :url "https://github.com/blosavio/trlisp"})

