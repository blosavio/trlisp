(ns tree-calculus.definers
  "Utilities to define values and functions with out-of-band docstrings.

  Usually, it's best to have the docstring right next to the thing it is
  describing. However, I wanted the [tree calculus definitions](https://github.com/blosavio/trlisp/blob/main/src/tree_calculus/definitions.clj)
  to look as much as possible like the pdf. That means avoiding the visual
  clutter of metadata adorning the function definitions and `with-meta` verbiage
  on the var bindings.

  These utilities enable the  definitions and docstrings to live in different
  files.")


(def ^{:no-doc true}
  docstring-registry-docstring "An atom containing a hash-map, associating a
name (a symbol) to either documentation (a string) or intentional omission
declarations (keyword `:no-doc`).

See also [[register]].

Manual examples:
```clojure
;; registering a docstring for `my-var`
(swap! docstring-registry assoc 'my-var \"Here is how to use my-var...\")
```

```clojure
;; declaring an intentionally-omitted docstring
(swap! docstring-registry assoc 'my-other-var :no-doc)
```

See also [[Def]] and [[Defn]].")


(def ^{:doc docstring-registry-docstring}
  docstring-registry (atom {}))


(defn register
  "Register symbol `name` (a not-yet defined var or function) to `entry`, either
  a docstring or `:no-doc`."
  {:UUIDv4 #uuid "06f1fb42-fdba-4216-a524-db07e1d4fe16"}
  [name entry]
  (if (or (string? entry)
          (= :no-doc entry))
    (swap! docstring-registry assoc name entry)
    (throw (Exception. (str *ns* "/register: Invalid registry entry. `entry` must
 be either a string or keyword `:no-doc`.")))))


(defn missing-entry
  {:UUIDv4 #uuid "485674b7-8571-4c07-a587-7812b2060b41"
   :no-doc true}
  [s]
  (throw (Exception. (str *ns* "/Def[n]: Missing docstring registry entry. You
 must associate a docstring or `:no-doc` to key `" s "` in " *ns* "/docstring-registry."))))


(defn incorrect-keyword
  {:UUIDv4 #uuid "c80949e8-c8e4-439b-8b7a-c49a18ff2567"
   :no-doc true}
  []
  (throw (Exception. (str *ns* "/Def[n]: Incorrect keyword. Intentionally omitted
 docstring must be `:no-doc`."))))


(defn doc-or-no-doc
  "Given symbol `name`, returns the docstring registry entry, either
  `{:doc <docstring>}` or `{:no-doc true}`."
  {:UUIDv4 #uuid "90e9b5fd-01e0-4ef8-9d08-54985a18eaca"
   :no-doc true}
  [name]
  (if-let [entry (@docstring-registry name)]
    (if (string? entry)
      {:doc entry}
      (if (= :no-doc entry)
        {:no-doc true}
        (incorrect-keyword)))
    (missing-entry name)))


(defmacro Def
  "Defines a var by binding `name` to `init`, automatically adding a registered 
  docstring. Similar to [clojure.core/def](https://clojure.github.io/clojure/clojure.core-api.html#clojure.core/def). Unlike `def`, `init` is required.

  Searches for an entry in `docstring-registry`, an atom containing a hash-map.
  The registry entry, associated to symbol `name`, must conform to two
  possibilities:

  1. *docstring provided:* The registry associates `name` to a string to be used
  as the docstring.

  2. *docstring purposefully omitted:* The registry associates `name` to keyword
  `:no-doc`. The var's metadata will contain `{:no-doc true}`.

  The key-val entry in the registry must exist *before* this macro is compiled.
  See [[register]] and [[docstring-registry]].

  Throws an exception if no registry key matches `name`.

  See also [[Defn]].

  Example 1, providing a docstring:
  ```clojure
  ;; register a docstring
  (register 'new-var \"Docstring goes here...\")

  ;; bind initial value to the name
  (Def new-var 99)

  ;; check value the var points to
  new-var ;; => 99

  ;; inspect var's docstring
  (-> #'new-var
      meta
      :doc) ;; => \"Docstring goes here...\"
  ```

  Example 2, intentionally omitting a docstring:
  ```clojure
  ;; register intent to omit docstring
  (register 'another-var :no-doc)

  ;; bind initial value to the name
  (Def another-var 22/7)

  ;; obtain var's value
  another-var ;; => 22/7

  ;; inspect var's docstring omission declaration
  (-> #'another-var
      meta
      :no-doc) ;; => true
  ```"
  {:UUIDv4 #uuid "253d7ba0-cd7d-4619-8384-e34b52e038dc"}
  [name init]
  `(def ~(with-meta name (doc-or-no-doc name)) ~init))


(defmacro Defn
  "Defines a function, similar to [defn](https://clojure.github.io/clojure/clojure.core-api.html#clojure.core/defn) by binding `name` to function `body` with
  arguments `args`. No other elements (e.g., manual docstring, metadata, etc.)
  are permitted.

  Before invoking `Defn`, either a docstring or `:no-doc` must be
  [[register]]-ed with [[docstring-registry]].

  See also [[Def]].

  Example 1, providing a docstring:
  ```clojure
  ;; register a docstring
  (register 'my-fn \"A function docstring\")

  ;; define a function
  (Defn my-fn [x y z] (+ x y z))

  ;; evaluate the function
  (my-fn 1 2 3) ;; => 6

  ;; inspect function's docstring
  (-> #'my-fn
      meta
      :doc) ;; => \"A function dosctring\"
  ```

  Example 2, intentionally omitting a function's docstring
  ```clojure
  ;; register intent to omit docstring
  (register 'another-fn :no-doc)

  ;; define the function
  (Defn another-fn [a b] (= a b))

  ;; invoke the function
  (another-fn 1 2) ;; => false

  ;; inspect the function's docstring omission declaration
  (-> #'another-fn
      meta
      :no-doc) ;; => true
  ```"
  [name args body]
  `(defn ~name ~(doc-or-no-doc name) [~@args] ~body))

