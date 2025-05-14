(ns tree-calculus.utilities-test
  (:require [clojure.test :refer [are
                                  deftest
                                  is
                                  run-tests
                                  testing]]
            [tree-calculus.definitions :refer :all]
            [tree-calculus.utilities :refer :all]))


(deftest Anything-to-Not-tests
  (testing "basic return"
    (are [x] (= Not x)
      (Anything-to-Not :a)
      (Anything-to-Not True)))
  (testing "usage in an expression"
    (are [x] (= True x)
      ((Anything-to-Not False) (I False))
      (D I Anything-to-Not False)
      (S Anything-to-Not I False))))


(deftest nat->tree-tests
  (are [x y] (= x y)
    (nat->tree 0) []
    (nat->tree 1) [[] []]
    (nat->tree 2) [[] [[] []]]
    (nat->tree 3) [[] [[] [[] []]]]))


(deftest tree->nat-tests
  (are [x y] (= x y)
    0 (tree->nat Δ)
    1 (tree->nat (nat->tree 1))
    2 (tree->nat (nat->tree 2))
    3 (tree->nat (nat->tree 3))))


(deftest bit->Bit-tests
  (are [x y] (= x y)
    Δ (bit->Bit 2r0)
    (K Δ) (bit->Bit 2r1)))


(deftest Bit->bit-tests
  (are [x y] (= x y)
    2r0 (Bit->bit Δ)
    2r1 (Bit->bit (K Δ))))


(deftest Byte->Bite-tests
  (is (thrown? java.lang.Exception (Byte->Bite -1)))
  (is (thrown? java.lang.Exception (Byte->Bite 256)))
  (are [x y] (= x y)
    (Byte->Bite 0)
    [[] [[] [[] [[] [[] [[] [[] [[] []]]]]]]]]

    (Byte->Bite 1)
    [[] [[] [[] [[] [[] [[] [[] [[[] []] []]]]]]]]]

    (Byte->Bite 2)
    [[] [[] [[] [[] [[] [[] [[[] []] [[] []]]]]]]]]

    (Byte->Bite 4)
    [[] [[] [[] [[] [[] [[[] []] [[] [[] []]]]]]]]]
    (Byte->Bite 255)
    [[[] []] [[[] []] [[[] []] [[[] []] [[[] []] [[[] []] [[[] []] [[[] []] []]]]]]]]]))


(deftest Empty?-tests
  (are [x y] (= x y)
    true (Empty? (List))
    false (Empty? (List :a))))


(deftest List->seq-tests
  (are [x y] (= x y)
    [] (List->seq (List))
    [:a] (List->seq (List :a))
    [:a :b :c] (List->seq (List :a :b :c))))


(deftest Bite->Byte-tests
  (are [x] (= x (Bite->Byte (Byte->Bite x)))
    0
    1
    2
    4
    255))


(deftest str->String-&-String->str-tests
  (are [s] (= s (String->str (str->String s)))
    ""
    "abc"
    "d!e@f#"))


#_(run-tests)
