(ns readme-utilities-tests
  "Tests for trlisp-specific ReadMe utilities."
  (:require
   [clojure.test :refer [are is deftest run-tests testing]]
   [readme-utilities :refer :all]))


(deftest replace-leaf-tests
  (testing "no matches"
    (are [x] (= x (replace-leaf x))
      "abc[abc]"))
  (testing "one match"
    (are [x y] (= x y)
      "Δ" (replace-leaf "[]")
      "abcΔabc" (replace-leaf "abc[]abc")))
  (testing "multiple matches"
    (are [x y] (= x y)
      "Δ Δ" (replace-leaf "[] []")
      "abcΔabcΔabc"(replace-leaf "abc[]abc[]abc"))))


(deftest replace-stem-tests
  (testing "no matches"
    (are [x] (= x (replace-stem x))
      "abc[]abc"))
  (testing "one match"
    (are [x y] (= x y)
      "K" (replace-stem "[[]]")
      "abcKabc"(replace-stem "abc[[]]abc")))
  (testing "multiple matches"
    (are [x y] (= x y)
      "abcKabcKabc" (replace-stem "abc[[]]abc[[]]abc"))))


(deftest replace-fork-tests
  (testing "no matches"
    (are [x] (= x (replace-fork x))
      "abc[]abc"))
  (testing "one match"
    (are [x y] (= x y)
      "(K Δ)" (replace-fork "[[] []]")
      "abc(K Δ)abc" (replace-fork "abc[[] []]abc")))
  (testing "multiple matches"
    (are [x y] (= x y)
      "abc(K Δ)abc(K Δ)abc" (replace-fork "abc[[] []]abc[[] []]abc"))))


(deftest replace-true-tests
  (testing "no matches"
    (are [x] (= x (replace-true x))
      "abc[]abc"))
  (testing "one match"
    (are [x y] (= x y)
      "True" (replace-true "[[]]")
      "abcTrueabc" (replace-true "abc[[]]abc")))
  (testing "multiple matches"
    (are [x y] (= x y)
      "abcTrueabcTrueabc" (replace-true "abc[[]]abc[[]]abc"))))


(deftest replace-false-tests
  (testing "no matches"
    (are [x] (= x (replace-false x))
      "abc[]abc"))
  (testing "one match"
    (are [x y] (= x y)
      "False" (replace-false "[[] [[[]] [[]]]]")
      "abcFalseabcFalseabc" (replace-false "abc[[] [[[]] [[]]]]abc[[] [[[]] [[]]]]abc"))))


(deftest make-stem-tests
  (testing "no-arg stems"
    (are [x y] (= x y)
      (make-stem :x)        " Δ\n |\n:x"      
      (make-stem "a")         "Δ\n|\na"
      (make-stem "ab")      " Δ\n |\nab"
      (make-stem "abc")     " Δ\n |\nabc"
      (make-stem "abde")  "  Δ\n  |\nabde"
      (make-stem "abdef") "  Δ\n  |\nabdef"))
  (testing "stems applied to an arg"
    (are [x y] (= x y)
      (make-stem :x :y) " Δ\n |  ␣  :y\n:x"
      (make-stem "a" "b") "Δ\n|  ␣  b\na")))


(deftest make-fork-tests
  (testing "no-arg forks"
    (are [x y] (= x y)
      (make-fork :x :y) "   Δ\n  / \\\n:x   :y"
      (make-fork "a"     "b")             "  Δ\n / \\\na   b"
      (make-fork "ab"    "c")           "   Δ\n  / \\\nab   c"
      (make-fork "ab"    "cd")          "   Δ\n  / \\\nab   cd"
      (make-fork "abc"   "d")         "    Δ\n   / \\\nabc   d"
      (make-fork "abc"   "de")        "    Δ\n   / \\\nabc   de"
      (make-fork "abcd"  "d")       "     Δ\n    / \\\nabcd   d"
      (make-fork "abcde" "vwxyz") "      Δ\n     / \\\nabcde   vwxyz"))
  (testing "forks applied to an arg"
    (are [x y] (= x y)
      (make-fork :x :y :z)  "   Δ\n  / \\   ␣  :z\n:x   :y"
      (make-fork "a" "b" "c") "  Δ\n / \\   ␣  c\na   b")))


(deftest make-fork-stem-tests
  (is (= (make-fork-stem "a" "b") "  Δ\n / \\\nΔ   b\n|\na"))
  (is (= (make-fork-stem "a" "b" "c") "  Δ\n / \\\nΔ   b  ␣  c\n|\na")))


(deftest make-fork-fork-tests
  (is (= (make-fork-fork "a" "b" "c") "    Δ\n   / \\\n  Δ   c\n / \\\na   b"))
  (is (= (make-fork-fork "a" "b" "c" "d") "    Δ\n   / \\\n  Δ   c  ␣  d\n / \\\na   b")))


(run-tests)

