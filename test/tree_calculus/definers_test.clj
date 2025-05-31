(ns tree-calculus.definers-test
  "Tests for alternative definers.

  To insert a unicode Delta:
  C-x 8, then ENTER, then type 0394"
  (:require
   [clojure.test :refer [are is deftest testing run-tests run-test]]
   [tree-calculus.definers :refer :all]))


(register 'definition-test-1 "abc")
(register 'definition-test-2 :no-doc)


(Def definition-test-1 101)
(Def definition-test-2 "abc")


(deftest register-tests
  (testing "normal registration"
    (are [x y] (= x y)
      "abc" ('definition-test-1 @docstring-registry)
      :no-doc ('definition-test-2 @docstring-registry)))
  (testing "invalid registration"
    (is (thrown? Exception (register 'incorrect-entry-1 99)))
    (is (thrown? Exception (register 'incorrect-entry-2 :invalid-key)))))


(deftest Def-tests
  (testing "correct invocations, binding values"
    (are [x y] (= x y)
      101 definition-test-1
      "abc" definition-test-2))
  (testing "correct invocations, setting metadata"
    (are [x y] (= x y)
      "abc" (:doc (meta #'definition-test-1))
      true (:no-doc (meta #'definition-test-2))))

  ;; can not catch a compile-time exception
  ;; https://stackoverflow.com/questions/32413662/how-to-catch-a-compile-time-exception
  
  #_(testing "incorrect invovation, no registry entry"
      (is (thrown? Exception (Def incorrect-test-1 :foo)))
      (is (thrown? Exception (Def incorrect-entry :foo)))))


(register 'function-test-1 "ghi")
(register 'function-test-2 :no-doc)


(Defn function-test-1
  [a b c]
  (map inc [1 2 3]))


(Defn function-test-2
  [a b]
  (= a b))


(deftest Defn-tests
  (testing "competancy of defining the fuction"
    (are [x y] (= x y)
      [2 3 4] (function-test-1 1 2 3)
      true (function-test-2 :foo :foo)))
  (testing "setting metadata"
    (are [x y] (= x y)
      "ghi" (:doc (meta #'function-test-1))
      true (:no-doc (meta #'function-test-2)))))


(run-tests)

