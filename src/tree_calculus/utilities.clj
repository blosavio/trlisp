(ns tree-calculus.utilities
  "Helper definitions, mostly for testing and readability, but not fundamental."
  (:require
   [clojure.set :refer [map-invert]]
   [tree-calculus.definitions :refer :all]))


(def ΔΔ (Δ Δ))


(def ΔΔΔ (Δ Δ Δ))


(def Anything-to-Not (K Not))


(defn nat->tree
  "Given natural number `nat`, returns a tree representation.

  See also [[tree->nat]]."
  {:UUIDv4 #uuid "b43667f4-70d3-4db3-893e-bc40209dc7fe"}
  ([n] (nat->tree n Δ))
  ([n t] (if (zero? n)
           t
           (nat->tree (dec n) (Δ Δ t)))))


(defn tree->nat
  "Given un-labelled binary tree `t`, returns equivalent natural number.

  See also [[nat->tree]]."
  {:UUIDv4 #uuid "62a54ee5-816e-48ae-8992-e1dac876578a"}
  ([t] (if (= t Δ)
         0
         (tree->nat (second t) 1)))
  ([t n] (if (= t Δ)
           n
           (tree->nat (second t) (inc n)))))


(def bit->Bit-metadata {:doc "Given a Clojure/Java bit, returns a tree calculus
 Bit.

See also [[Bit->bit]]."
                        :UUIDv4 #uuid "99d4d42b-6f9e-466c-bf21-ad6cf618fad3"})


(def ^bit->Bit-metadata
  bit->Bit
  {2r0 Δ
   2r1 (K Δ)})


(def Bit->bit-metadata {:doc "Given a tree calculus Bit, returns a Clojure/Java
 bit.

See also [[bit->Bit]]."
                        :UUIDv4 #uuid "b8c4b17c-e7bc-4651-9a64-dda4a323a531"})


(def ^Bit->bit-metadata
  Bit->bit
  (map-invert bit->Bit))


(defn Byte->Bite
  "Given a Clojure/Java Byte `B`, returns a tree calculus Bite.

  See also [[Bite->Byte]]."
  {:UUIDv4 #uuid "d6194cbc-f42f-4d0e-beca-67dd7a6f4b9b"}
  [B]
  (let [mn (int 0)
        mx (int (- (Math/pow 2 8) 1))
        to-bin (fn [i]
                 (-> i
                     (Integer/toString 2)
                     (Integer/parseInt)
                     (->> (format "%08d"))))
        to-bits (fn [b] (map parse-long (clojure.string/split b #"")))
        bits (to-bits (to-bin B))]
    (if (<= mn B mx)
      (apply List (map bit->Bit bits))
      (throw (Exception. (str "Value must be in range " mn " to " mx "."))))))


(defn Empty?
  "Returns `true` if tree calculus list `L` is empty."
  {:UUIDv4 #uuid "98ac6f41-b11f-4771-a8df-b9b6266f9d02"}
  [L]
  (= Δ L))


(defn List->seq
  "Given tree calculus List `L`, returns a Clojure sequence of elements."
  {:UUIDv4 #uuid "b944a40e-e58f-439b-a449-b5c27f735f9d"}
  ([L] (if (Empty? L)
         []
         (List->seq L [])))
  ([L acc]
   (if (= (second L) Δ)
     (conj acc (first L))
     (List->seq (second L) (conj acc (first L))))))


(defn Bite->Byte
  "Given a tree calculus Bite, returns a Clojure/Java Byte integer.

  See also [[Byte->Bite]]."
  {:UUIDv4 #uuid "744bbe71-8699-45e2-8af9-e10ed8e27adb"}
  [B]
  (let [bit-seq (List->seq B)]
    (if (not= 8 (count bit-seq))
      (throw (Exception. "Bite must contain exactly eight values."))
      (Integer/parseUnsignedInt
       (reduce #(str %1 %2) "" (map Bit->bit bit-seq))
       2))))


(defn str->String
  "Given Clojure/Java string `s`, returns a tree calculus String.

  See also [[String->str]]."
  {:UUIDv4 #uuid "12d4738a-90cc-4d60-ac30-54230e9bb9c1"}
  [s]
  (->> s
      char-array
      seq
      (map int)
      (map Byte->Bite)
      (apply List)))


(defn String->str
  "Given tree calculus String `S`, returns a Clojure/Java string.

  See also [[str->String]]."
  {:UUIDv4 #uuid "95381ae1-2743-4ea3-aea1-d3284b1a2263"}
  [S]
  (->> S
       List->seq
       (map Bite->Byte)
       (map char)
       (clojure.string/join)))

