
  <body>
    <a href="https://clojars.org/com.sagevisuals/trlisp"><img src="https://img.shields.io/clojars/v/com.sagevisuals/trlisp.svg"></a><br>
    <a href="#setup">Setup</a><br>
    <a href="https://blosavio.github.io/trlisp/index.html">API</a><br>
    <a href="https://github.com/blosavio/trlisp/blob/main/changelog.md">Changelog</a><br>
    <a href="#intro">Introduction</a><br>
    <a href="#usage">Usage</a><br>
    <a href="#fundamental">Fundamental combinators</a><br>
    <a href="#base">Base functions</a><br>
    <a href="#comments">Comments</a><br>
    <a href="#references">References</a><br>
    <a href="#glossary">Glossary</a><br>
    <a href="https://github.com/blosavio">Contact</a><br>
    <h1>
      trlisp
    </h1><em>A Lisp-y implementation of B. Jay&apos;s unlabelled binary tree calculus</em><br>
    <section id="setup">
      <h2>
        Setup
      </h2>
      <h3>
        Leiningen/Boot
      </h3>
      <pre><code>[com.sagevisuals/trlisp &quot;0-SNAPSHOT0&quot;]</code></pre>
      <h3>
        Clojure CLI/deps.edn
      </h3>
      <pre><code>com.sagevisuals/trlisp {:mvn/version &quot;0-SNAPSHOT0&quot;}</code></pre>
      <h3>
        Require
      </h3>
      <pre><code>(require &apos;[tree-calculus.definitions :refer :all])</code></pre>
    </section>
    <section id="intro">
      <h2>
        Introduction
      </h2>
      <p>
        Barry Jay&apos;s unlabelled binary <a href="https://github.com/barry-jay-personal/tree-calculus/blob/master/tree_book.pdf">tree calculus</a> is
        <em>reflective</em>, which means we can inspect the internal structure of every &nbsp;function. Without quotation, every function is always open to:
      </p>
      <ul>
        <li>
          <strong>Inspection</strong> We may view the function&apos;s structure and &nbsp;contents.
        </li>
        <li>
          <strong>Analysis</strong> We may measure the function&apos;s size, etc.
        </li>
        <li>
          <strong>Comparison</strong> We may test two functions for equality, &nbsp;equivalence, etc.
        </li>
        <li>
          <strong>Optimization</strong> We may precompute some static branch of the &nbsp;function, or alter it for more efficiency.
        </li>
        <li>
          <strong>Decomposition</strong> We may pull out different pieces of the &nbsp;function, perhaps for use in a different context.
        </li>
        <li>
          <strong>Modification</strong> We may change some facet of the function at &nbsp;runtime to suit our needs.
        </li>
      </ul>
      <p></p>
      <p>
        <em>trlisp</em> is an implementation of tree calculus with Lisp&apos;s compact, &nbsp;uniform grammar. The element at the head of a list, the function
        position, is &nbsp;invoked, with the elements of the tail of the list providing the arguments.
      </p>
      <p>
        Unlike traditional Lisps, trlisp functions may be transparently decomposed &nbsp;so that the function may be inspected, analyzed, compared, optimized,
        &nbsp;decomposed, and modified, without the need for quotation. That is to say, &nbsp;trlisp functions are reflective.
      </p>
      <p>
        <em>trlisp</em> is intended to be used to explore practical questions such &nbsp;as
      </p>
      <ul>
        <li>How useful is it to have unquoted access to the contents of a function?
        </li>
        <li>Can new functions be composed from pulled-apart pieces of others?
        </li>
        <li>Could a function be automatically inverted?
        </li>
      </ul>
      <p></p>
    </section>
    <section id="usage">
      <h2>
        Usage
      </h2>
      <h3>
        Lisp-y
      </h3>
      <section id="lisp">
        <p>
          <em>trlisp</em> inherits its evaluation model from its host, Clojure, a &nbsp;Lisp dialect. In Lisp, the fundamental evaluation unit is a list
          enclosed in &nbsp;parentheses. The first element of the list is the function, and any trailing &nbsp;list elements are the function&apos;s arguments,
          like this.
        </p>
        <pre><code>(function argument-1 argument-2 ... argument-n)</code></pre>
        <p>
          If we&apos;d like to sum the integers two and three, we&apos;d write this.
        </p>
        <pre><code>(+ 2 3)</code></pre>
        <p>
          In this example, the parentheses contain the expression we&apos;d like to &nbsp;evaluate. The symbol for addition, <code>+</code>, is the first
          &nbsp;element, so it serves as the function. The trailing elements, integers <code>2</code> and <code>3</code>, supply the two arguments to
          <code>+</code>.
        </p>
        <p>
          When we evaluate the expression <code>(+ 2 3)</code>, we obtain a &nbsp;value, integer <code>5</code>.
        </p>
        <pre><code>(+ 2 3) ;; =&gt; 5</code></pre>
        <p>
          trlisp follows this model. Parenthesized lists form a unit of evaluation, &nbsp;with the function at the head of the list applying to zero or more
          arguments &nbsp;in the tail.
        </p>
      </section>
      <h3>
        <code>Δ</code>, the do-everything operator
      </h3>
      <p>
        A notable aspect of tree calculus is that every <em>thing</em>, functions as well as values, is composed of a single, basic unit, <code>Δ</code>. So we
        must continually keep in mind that there is not a strict delineation &nbsp;between tree calculus functions, such as the <code>+</code> function, and
        tree calculus scalar values, such as integer <code>3</code>. Every entity in tree calculus is a binary tree composed only of different &nbsp;patterns
        of <code>Δ</code>. The application rules govern how one or two trees produce another tree.
      </p>
      <p>
        Tree calculus defines six application rules. Five rules are the result &nbsp;of dispatching on whether the tree in the function position of the list is
        a &nbsp;leaf, stem, or fork, the last of which further dispatches on whether the left &nbsp;child is a leaf, stem, or fork.
      </p>
      <p>
        The sixth rule is merely the degenerate case with zero arguments, which &nbsp;we&apos;ll discuss first.
      </p>
      <h4 id="apply-leaf-to-nothing">
        Rule&nbsp;0: Applying a leaf to nothing
      </h4>
      <p>
        Let&apos;s imagine a picture of the first rule: applying a leaf to nothing.
      </p>
      <pre><code>Δ</code></pre>
      <p>
        It doesn&apos;t look like much. Just a Greek delta character floating on the &nbsp;page, but as the rules progress and we add more elements, our
        picture will &nbsp;become more informative.
      </p>
      <p>
        Applying the <code>Δ</code> operator to zero arguments returns merely itself. We put the node symbol in &nbsp;function position of the list, followed
        by zero arguments, and evaluate the &nbsp;expression.
      </p>
      <pre><code>(Δ)</code></pre>
      <p>
        This returns a single tree node with zero children.
      </p>
      <p>
        Often, a function definition will involve a bare leaf in the middle of a &nbsp;sequence of applications, so those parentheses might get visually
        distracting. &nbsp;If we only need a bare leaf, we may skip the parentheses.
      </p>
      <pre><code>(= (Δ) Δ) ;; =&gt; true</code></pre>
      <p>
        We can see that <code>(Δ)</code> and <code>Δ</code> are equivalent.
      </p>
      <h4 id="apply-leaf-to-something">
        Rule&nbsp;1: Applying a leaf to something
      </h4>
      <p>
        Our picture grows with Rule&nbsp;1. Since we are applying a leaf to something, &nbsp;we&apos;ll imagine a leaf as before, plus something else standing
        to its right. &nbsp;Let&apos;s call it <code>z</code>.
      </p>
      <pre><code>Δ     z</code></pre>
      <p>
        <code>z</code> will be some unspecified tree that can be operated on, &nbsp;either another leaf, a stem, or some unspecified fork.
      </p>
      <p>
        To make our picture exceedingly explicit, let&apos;s introduce a symbol <code>␣</code> to visually separate the function and the argument.
      </p>
      <pre><code>Δ  ␣  z</code></pre>
      <p>
        We can interpret that picture as <em>Function <code>Δ</code> applied to argument <code>z</code>.</em>
      </p>
      <p>
        As with all Lisps, trlisp uses parentheses for function invocation. To &nbsp;evaluate that expression, we put the function <code>Δ</code> at the head
        of the list and the single argument <code>z</code> in the tail.
      </p>
      <pre><code>(Δ z)</code></pre>
      <p>
        A Lisp programmer would read that expression as <em>Function <code>Δ</code> applied to argument <code>z</code></em>, which is the same interpretation
        we just gave our picture.
      </p>
      <p>
        When the <code>Δ</code> function is evaluated with one argument, a stem &nbsp;results with the argument forming the single child branch. Here is a
        picture of &nbsp;the result.
      </p>
      <pre><code>Δ
|
z</code></pre>
      <p>
        The expression returns a stem: a root node at the top, with a single child &nbsp;element <code>z</code> branching straight off its bottom.
      </p>
      <h4 id="apply-stem">
        Rule&nbsp;2: Applying a stem
      </h4>
      <p>
        Let&apos;s visualize applying a stem to something.
      </p>
      <pre><code>Δ
|  ␣  z
x</code></pre>
      <p>
        The stem, serving as our function, is on the left and applies to the &nbsp;argument immediately to its right, the <code>z</code>, which may be any
        valid tree.
      </p>
      <p>
        To evaluate this in trlisp, we put a stem in the function position and the &nbsp;argument in the tail. Let&apos;s take it step by step. To compose the
        function, a &nbsp;stem, we&apos;ll recall the <a href="#apply-leaf-to-something">previous subsection</a> where we constructed a stem by applying a leaf
        node to something.
      </p>
      <pre><code>(Δ x)</code></pre>
      <p>
        The stem we just now composed consists of a root node at top and an &nbsp;unspecified child tree <code>x</code>.
      </p>
      <p>
        Now we have a stem in hand to serve as a function. We place that stem at &nbsp;the head of our list, followed by the argument, some unspecified tree
        <code>z</code>.
      </p>
      <pre><code>((Δ x) z)</code></pre>
      <p>
        Tree calculus declares that evaluating that expression yields a fork, &nbsp;with <code>x</code> forming the left child branch and <code>z</code>
        forming the right child branch.
      </p>
      <p>
        We could visualize the result like this.
      </p>
      <pre><code>  Δ
&nbsp;/ \
x   z</code></pre>
      <p>
        Root node at top and two children, in order, branching off the bottom.
      </p>
      <p>
        An <a href="#variadic">upcoming section</a> discusses ways to write this more efficiently.
      </p>
      <p>
        Note that application Rules&nbsp;0, 1, and&nbsp;2 form a basis for &nbsp;constructing trees. Applying a bare leaf to nothing returns a leaf. Applying a
        &nbsp;leaf to something creates a stem. And applying a stem creates a fork.
      </p>
      <p>
        Application Rules&nbsp;3, 4, and&nbsp;5 involve applying three varieties of &nbsp;forks. When applying a fork, trlisp dispatches on whether the left
        child itself &nbsp;is a leaf, stem, or fork.
      </p>
      <h4 id="apply-fork-leaf">
        Rule&nbsp;3: Applying a fork with left child leaf
      </h4>
      <p>
        Here is a picture of applying a fork, where the left child is a leaf, to &nbsp;an argument <code>z</code>.
      </p>
      <pre><code>  Δ
&nbsp;/ \   ␣  z
Δ   y</code></pre>
      <p>
        Both the right child branch <code>y</code> and argument <code>z</code> can be any valid tree.
      </p>
      <p>
        To express this application in trlisp, let&apos;s first construct the function, &nbsp;a fork. We know how to construct a fork from the <a href=
        "#apply-stem">previous subsection</a>. A fork is constructed by applying a stem to something, and a stem itself is &nbsp;constructed by applying a leaf
        to something. We&apos;ll do each step in turn.
      </p>
      <p>
        The left branch of our function is a leaf, so we need a root node and a &nbsp;child <code>Δ</code>. To construct a stem, we apply a leaf to another
        leaf.
      </p>
      <pre><code>(Δ Δ)</code></pre>
      <p>
        We visualize that resulting stem like this.
      </p>
      <pre><code>Δ
|
Δ</code></pre>
      <p>
        Now that we have a stem, we know from Rule&nbsp;2 that applying a stem to &nbsp;something constructs a fork. So we can apply that stem to some branch
        <code>y</code>.
      </p>
      <pre><code>((Δ Δ) y)</code></pre>
      <p>
        Which produces a tree that will serve as our function.
      </p>
      <pre><code>  Δ
&nbsp;/ \
Δ   y</code></pre>
      <p>
        A root node at top, a leaf node is the left child, and some subtree <code>y</code> is the right child.
      </p>
      <p>
        Finally, we insert this function at the head of the list and supply <code>z</code> as the argument in the tail position.
      </p>
      <pre><code>(((Δ Δ) y) z)</code></pre>
      <p>
        Tree calculus declares that if the left child of a fork is a leaf, &nbsp;application of that fork to anything returns the fork&apos;s right branch and
        &nbsp;discards the argument. In this case, the function&apos;s right child is <code>y</code>, which is returned. Argument <code>z</code> is discarded.
      </p>
      <p>
        One way to look at this is the sequential application of a stem to two &nbsp;arguments <code>y</code>, then <code>z</code>.
      </p>
      <pre><code>Δ
|  ␣  y  ␣  z
Δ</code></pre>
      <p>
        This is a pattern that we see repeatedly in tree calculus: Applying a &nbsp;stem to two arguments selects the first of two arguments <code>y</code> and
        discards the second argument <code>z</code>.
      </p>
      <p id="forgiving">
        trlisp strives to be forgiving when it comes to parentheses. We &nbsp;can discard any pair of parentheses if the implied left-association holds. The
        &nbsp;following
      </p>
      <pre><code>(((Δ Δ) y) z)</code><br><code> ((Δ Δ) y  z)</code><br><code> ((Δ Δ  y) z)</code><br><code>  (Δ Δ  y  z)</code></pre>
      <p>
        are all equivalent: All return <code>y</code>.
      </p>
      <h4 id="apply-fork-stem">
        Rule&nbsp;4: Applying a fork with left child stem
      </h4>
      <p>
        Here is a picture of a function composed of a fork with a left child &nbsp;stem.
      </p>
      <pre><code>  Δ
&nbsp;/ \
Δ   y  ␣  z
|
x</code></pre>
      <p>
        Working up from the bottom, the left child stem is itself composed of a &nbsp;node with some unspecified tree <code>x</code> as it single child. To
        make this stem
      </p>
      <pre><code>Δ
|
x</code></pre>
      <p>
        we evaluate
      </p>
      <pre><code>(Δ x)</code></pre>
      <p>
        Then, to make our desired fork, we supply that stem as the left child &nbsp;branch and some unspecified tree <code>y</code> as the right child branch.
      </p>
      <pre><code>(Δ (Δ x) y)</code></pre>
      <p>
        Which evaluates to this picture.
      </p>
      <pre><code>  Δ
&nbsp;/ \
Δ   y
|
x</code></pre>
      <p>
        See the <a href="#forgiving">note</a> at the end of the previous subsection on why we&apos;re allowed to sometimes drop &nbsp;parentheses.
      </p>
      <p>
        Finally, we apply this function to some unspecified tree as argument <code>z</code> by inserting the function at the head of the list and the
        &nbsp;argument in the tail.
      </p>
      <pre><code>((Δ (Δ x) y) z)</code></pre>
      <p>
        Tree calculus declares this expression to evaluate to
      </p>
      <pre><code>((y z) (x z))</code></pre>
      <p>
        The order of the function&apos;s right child <code>y</code> and left grandchild <code>x</code> are swapped, and the argument <code>z</code> is
        broadcast to each. Then the result of the first evaluation is applied to &nbsp;the result of the second evaluation. This pattern often appears in
        definitions &nbsp;because it&apos;s useful to be able to swap two items.
      </p>
      <h4 id="apply-fork-fork">
        Rule&nbsp;5: Applying a fork with left child fork
      </h4>
      <p>
        The final application rule involves a function whose left fork is itself a &nbsp;fork. Let&apos;s visualize that situation.
      </p>
      <pre><code>    Δ
&nbsp;  / \
&nbsp; Δ   y  ␣  z
&nbsp;/ \
w   x</code></pre>
      <p>
        Examining the function on the left, we see a child fork with two branches, &nbsp;designated <code>w</code> and <code>x</code>. Using what we&apos;ve
        discussed so far, let&apos;s construct this function from the &nbsp;bottom up. We know how to construct a fork, so let&apos;s make one with a left
        &nbsp;branch some unspecified <code>w</code> and right branch some unspecified <code>x</code>.
      </p>
      <pre><code>(Δ w x)</code></pre>
      <p>
        That trlisp expression creates this sub-tree.
      </p>
      <pre><code>  Δ
&nbsp;/ \
w   x</code></pre>
      <p>
        With that sub-tree in hand, let&apos;s move up one level. That sub-tree becomes &nbsp;the left child of the root node, while some unspecified tree
        <code>y</code> becomes the right child node.
      </p>
      <pre><code>(Δ (Δ w x) y)</code></pre>
      <p>
        Now we have this picture.
      </p>
      <pre><code>    Δ
&nbsp;  / \
&nbsp; Δ   y
&nbsp;/ \
w   x</code></pre>
      <p>
        This is our function, which we insert into the head of our trlisp &nbsp;expression, followed by some unspecified argument <code>z</code>
      </p>
      <pre><code>((Δ (Δ w x) y) z)</code></pre>
      <p>
        to obtain our final expression.
      </p>
      <p>
        Tree calculus declares that this expression evaluates to the following.
      </p>
      <pre><code>(z w x)</code></pre>
      <p>
        Applying a fork with a left child fork results in making the argument <code>z</code> the new function, with the decomposition of the function&apos;s
        left child&apos;s &nbsp;branches <code>w</code> and <code>x</code> the new arguments. This pattern is useful when we need to pull apart a fork
        &nbsp;and send the pieces to a function.
      </p>
      <p>
        The next two subsections introduce a few conveniences.
      </p>
      <h4 id="variadic">
        Variadic application
      </h4>
      <p>
        trlisp provides a convenience to minimize parentheses: We may supply any &nbsp;number of arguments and trlisp will sequentially apply them by
        &nbsp;left-association. For example, when we evaluate the <code>Δ</code> function with two arguments, we obtain a fork. Let&apos;s supply arguments
        <code>x</code> and <code>y</code>, two unspecified trees.
      </p>
      <pre><code>(Δ x y)</code></pre>
      <p>
        When evaluated, this returns a fork, a node whose left child is some &nbsp;tree <code>x</code> and whose right child is <code>y</code>, some other
        tree. Under the hood, trlisp applied <code>Δ</code> to <code>x</code>, then applied that result to <code>y</code>, exactly as if we had evaluated
      </p>
      <pre><code>((Δ x) y)</code></pre>
      <p>
        This principle can be extended as needed. Five applications
      </p>
      <pre><code>(((((Δ Δ) Δ) Δ) Δ) Δ)</code></pre>
      <p>
        may be more succinctly written like this
      </p>
      <pre><code>(Δ Δ Δ Δ Δ Δ)</code></pre>
      <p>
        with the understanding that application is left-associative.
      </p>
      <h4>
        Variable binding and convenience functions
      </h4>
      <p>
        Usually, we&apos;ll find it convenient to forgo combinatory logic&apos;s goal of &nbsp;avoiding names. In those situations, we can rely on our host
        platform&apos;s &nbsp;variable binding to give meaningful names to commonly used trees. Clojure&apos;s <code>def</code> binds a name to a value.
      </p>
      <pre><code>(def foo (Δ Δ))</code></pre>
      <p>
        Here, we bound the Clojure symbol <code>foo</code> to a two-node stem. Now we can use <code>foo</code> in future expressions, and it will evaluate to
        <code>(Δ Δ)</code>.
      </p>
      <p>
        Also, we will sometimes find it convenient to use a lambda calculus-like &nbsp;expression. We can express
      </p>
      <pre><code>λa.λb.ab</code></pre>
      <p>
        as
      </p>
      <pre><code>(fn [a] (fn [b] (a b)))</code></pre>
      <p>
        in Clojure.
      </p>
      <p>
        In trlisp , such a function will appear hanging off a tree somewhere. We &nbsp;might see something like this.
      </p>
      <pre><code>    Δ
&nbsp;  / \
&nbsp; Δ   y  ␣  z
&nbsp;/ \
w   (fn [a] (fn [b] (a b)))</code></pre>
      <p>
        trlisp&apos;s application machinery handles that tree without any additional &nbsp;effort from us.
      </p>
      <h4 id="application-summary">
        Application rules quick summary
      </h4>
      <p>
        Here are all six application rules at once. Notice that the first three &nbsp;rules describe how to construct a leaf, stem, or fork. The last three
        rules &nbsp;describe how to evaluate the application of a fork with a left child leaf, a &nbsp;left child stem, and left child fork, respectively.
      </p>
      <table>
        <tr>
          <td>
            <p>
              0. Apply leaf to nothing.
            </p>
          </td>
          <td>
            <pre><code>Δ ␣ </code></pre>
          </td>
          <td>
            <pre><code> (Δ)      = Δ</code></pre>
          </td>
        </tr>
        <tr>
          <td>
            <p>
              1. Apply leaf to something.
            </p>
          </td>
          <td>
            <pre><code>Δ ␣ z</code></pre>
          </td>
          <td>
            <pre><code>((Δ)   z) = (Δ   z)</code></pre>
          </td>
        </tr>
        <tr>
          <td>
            <p>
              2. Apply stem.
            </p>
          </td>
          <td>
            <pre><code>Δ
|  ␣  z
x</code></pre>
          </td>
          <td>
            <pre><code>((Δ x) z) = (Δ x z)</code></pre>
          </td>
        </tr>
        <tr>
          <td>
            <p>
              3. Apply fork-leaf.
            </p>
          </td>
          <td>
            <pre><code>  Δ
&nbsp;/ \   ␣  z
Δ   y</code></pre>
          </td>
          <td>
            <pre><code>((Δ  Δ      y) z) = y</code></pre>
          </td>
        </tr>
        <tr>
          <td>
            <p>
              4. Apply fork-stem.
            </p>
          </td>
          <td>
            <pre><code>  Δ
&nbsp;/ \
Δ   y  ␣  z
|
x</code></pre>
          </td>
          <td>
            <pre><code>((Δ (Δ   x) y) z) = ((y z) (x z))</code></pre>
          </td>
        </tr>
        <tr>
          <td>
            <p>
              5. Apply fork-fork.
            </p>
          </td>
          <td>
            <pre><code>    Δ
&nbsp;  / \
&nbsp; Δ   y  ␣  z
&nbsp;/ \
w   x</code></pre>
          </td>
          <td>
            <pre><code>((Δ (Δ w x) y) z) = (z w x)</code></pre>
          </td>
        </tr>
      </table>
      <p>
        And that&apos;s it. One operator and six rules are all that is necessary for a &nbsp;combinatorially-complete system.
      </p>
      <h3 id="implementation">
        Implementation Note
      </h3>
      <p>
        Tree calculus trees have a dual nature: they are both structures and &nbsp;functions. That is, trees <em>contain</em> an arbitrary pattern of
        descendants, but at the same time, they &nbsp;conceptually <em>apply</em> a transform to an argument. Any implementation must therefore capture these
        &nbsp;two properties.
      </p>
      <p>
        Clojure vectors are handy because, while they are exceedingly capable at &nbsp;storing and retrieving arbitrary values, they support an interface that
        allows &nbsp;them to behave as a Lisp function. When a Clojure vector is placed in the &nbsp;function position of a list and an integer argument
        follows in the tail, the &nbsp;vector invokes an implied accessor function, <a href=
        "https://clojure.github.io/clojure/clojure.core-api.html#clojure.core/nth"><code>nth</code></a>, which returns the value at the index.
      </p>
      <p>
        For example, we could explicitly access the third element with <code>nth</code> like so.
      </p>
      <pre><code>;; plain Clojure</code><br><code>(nth [98 99 100] 2) ;; =&gt; 100</code></pre>
      <p>
        Equivalently, we could rely on a Clojure vector&apos;s ability to behave as a &nbsp;Lisp function.
      </p>
      <pre><code>;; plain Clojure</code><br><code>([98 99 100] 2) ;; =&gt; 100</code></pre>
      <p>
        Putting a vector at the head of a list is a widely-used Clojure idiom.
      </p>
      <p>
        Since Clojure hosts trlisp, vectors possess the machinery to serve as &nbsp;functions. We merely need to change that machinery from the accessor
        &nbsp;function <code>nth</code> to a different machine that implements tree calculus&apos; application rules.
      </p>
      <p>
        trlisp models a tree as an <a href="https://github.com/blosavio/thingy">altered Clojure vector</a> containing up to two other such altered vectors.
        These nested vectors &nbsp;provide the reified structure representing tree calculus&apos; unlabelled binary &nbsp;trees. Since trlisp&apos;s vectors
        are altered so that the invocation machinery &nbsp;implements tree calculus&apos; application rules, these nested vectors may serve as &nbsp;functions
        when placed at the head of a list.
      </p>
      <p>
        A single node is simply an empty vector, i.e., it has no descendants. We &nbsp;can observe this by evaluating a bare node.
      </p>
      <pre><code>(Δ) ;; =&gt; []</code></pre>
      <p>
        Applying the <code>Δ</code> function to nothing returns trlisp&apos;s literal representation, an empty &nbsp;vector.
      </p>
      <p>
        Let&apos;s consider a simple stem.
      </p>
      <pre><code>Δ
|
Δ</code></pre>
      <p>
        We can construct a node with a single child leaf by applying one node to &nbsp;another node.
      </p>
      <pre><code>(Δ Δ) ;; =&gt; [[]]</code></pre>
      <p>
        Now we see that the parent vector contains a single element, another &nbsp;vector of the same kind.
      </p>
      <p>
        We could have equivalently written the following.
      </p>
      <pre><code>([] []) ;; =&gt; [[]]</code></pre>
      <p>
        If these were Clojure&apos;s normal vectors, evaluating that expression would &nbsp;result in an error. But since trlisp uses a special vector that
        implements tree &nbsp;calculus&apos; application rules, it works fine.
      </p>
      <p>
        The first special vector in the function position <code>[]</code> applied to another special vector <code>[]</code> invokes tree calculus&apos;
        application rules. In this case, according to &nbsp;Rule&nbsp;1, a leaf (empty vector) applied to another tree (also a leaf in this &nbsp;case),
        constructs a stem.
      </p>
      <p>
        Extending this principle further, consider this simple fork.
      </p>
      <pre><code>  Δ
&nbsp;/ \
Δ   Δ</code></pre>We can construct this fork by supplying the parent node with two child nodes.
      <pre><code>(Δ Δ Δ) ;; =&gt; [[] []]</code></pre>
      <p>
        Here we see that the root vector now contains two child vectors.
      </p>
      <p>
        trlisp&apos;s main usefulness in exploring tree calculus is the fact that these &nbsp;special vectors invoke the tree calculus application rules
        instead of the &nbsp;built-in <code>nth</code>.
      </p>
      <p>
        One practical consequence of using vectors this way is that rendering &nbsp;trees as nested empty vectors is not very ergonomic for human eyes. After
        some &nbsp;practice, it&apos;s not too difficult to mentally translate <code>(Δ Δ Δ)</code> to
      </p>
      <pre><code>  Δ
&nbsp;/ \
Δ   Δ</code></pre>
      <p>
        but it requires much more eyestrain to see that both are equivalent to
      </p>
      <pre><code>[[] []]</code></pre>
      <p>
        And it only gets worse as the trees grow beyond three nodes.
      </p>
      <p>
        One option is to use Clojure keywords to represent arbitrary subtrees. &nbsp;The evaluation of this expression
      </p>
      <pre><code>(Δ :a :b) ;; =&gt; [:a :b]</code></pre>
      <p>
        can be visualized like this.
      </p>
      <pre><code>   Δ
&nbsp; / \
:a   :b</code></pre>
      <p>
        But we can&apos;t use this option when a Clojure keyword would end up in the &nbsp;function position of a tree application rule, like in Rule&nbsp;4,
      </p>
      <pre><code>;; this won&apos;t work</code><br><code>((:a :b) (:c :d))</code></pre>
      <p>
        because Clojure keywords don&apos;t implement the tree calculus application &nbsp;rules.
      </p>
      <p>
        In that case, we must define concrete trees to which to apply the rules.
      </p>
      <pre><code>(def h Δ)</code><br><code>(def i Δ)</code><br><code>(def j Δ)</code><br><code>(def k Δ)</code><br><br><code>((h i) (j k)) ;; =&gt; [[] [[]]]</code></pre>
      <p>
        The expression can now return a valid value, but we are still left with &nbsp;the problem of figuring out what the tree <code>[[]&nbsp;[[]]]</code>
        looks like.
      </p>
      <p>
        trlisp doesn&apos;t currently (and may never) have a utility which renders tree &nbsp;diagrams. When walking through our discussion, I will silently
        replace things &nbsp;like <code>[[]&nbsp;[[]]]</code> with its equivalent <code>(Δ&nbsp;Δ&nbsp;K)</code>.
      </p>
      <p>
        Just keep in mind that if you are evaluating trlisp expressions on your &nbsp;own computer, you will see <code>[[]&nbsp;[[]]]</code>.
      </p>
      <p>
        To illustrate how function trees apply to arguments trees and return &nbsp;other trees, we can test for equality with the expected value. For example,
        &nbsp;Rule&nbsp;2 tells us that a stem applied to an argument leaf returns a fork with &nbsp;the argument leaf as the right child.
      </p>
      <pre><code>((Δ Δ) Δ) ;; =&gt; [[] []]</code></pre>
      <p>
        That&apos;s...okay. But working it out with pencil and paper, we expect it to &nbsp;return <code>(Δ&nbsp;Δ&nbsp;Δ)</code>. Is that the case?
      </p>
      <pre><code>(= ((Δ Δ) Δ)
&nbsp;  (Δ Δ Δ)) ;; =&gt; true</code></pre>
      <p>
        Yup. The value returned from the original application, in the upper row, &nbsp;is equal to the value returned by the expression in the lower row. By
        directly &nbsp;comparing return values with <code>=</code>, we don&apos;t have to squint at a pile of nested square brackets.
      </p>
      <h3 id="conventions">
        Conventions
      </h3>
      <p>
        Clojure names are <code>lower-case-with-hyphens</code>, such as <code>inc</code>, <code>first</code>, and <code>map</code>. trlisp names are
        <code>Upper-Case-With-Hyphens</code>, such as <code>Inc</code>, <code>First</code>, and <code>Map</code>.
      </p>
      <p>
        The exceptions are:
      </p>
      <ul>
        <li>
          <p>
            <code>d</code>
          </p>
          <p>
            The D combinator already claims the capital &apos;D&apos;. Since the <code>d</code> function is used extensively in definitions, and it is so
            closely related &nbsp;to the D combinator, I chose to keep it as a single, lower-case &apos;d&apos;.
          </p>
        </li>
        <li>
          <p>
            <code>error</code>
          </p>
          <p>
            Java (Clojure&apos;s host) already uses <code>Error</code>.
          </p>
        </li>
        <li>
          <p>
            Conversion utilities that accept Clojure types (which are lower-cased) &nbsp;and return trees. E.g.,
          </p><code>str-&gt;String</code>.
        </li>
      </ul>
      <p></p>
      <p>
        Java claims <code>Byte</code> while Clojure claims <code>byte</code>, so trlisp uses <code>Bite</code> to denote eight binary digits.
      </p>
      <h3>
        Recommendations
      </h3>
      <p>
        First, work out the examples with a pencil and paper, and only then check &nbsp;their evaluations in trlisp. Letting the computer do all the work
        stymies a &nbsp;deeper understanding we might get by slinging around tree nodes by hand.
      </p>
      <p>
        The <a href="https://github.com/blosavio/trlisp/blob/main/test/tree_calculus/definitions_test.clj">unit testing</a> namespace (and its <a href=
        "https://github.com/blosavio/trlisp/blob/main/test/tree_calculus/exercising_test.clj">auxiliary</a>) contain working examples of every trlisp function.
      </p>
      <p>
        Finally, the <a href="https://github.com/barry-jay-personal/tree-calculus/blob/master/tree_book.pdf">tree calculus book</a> is the authoritative and
        complete reference on the subject. This <em>ReadMe</em> is merely a description of how to use trlisp to evaluate tree calculus &nbsp;expressions. Use
        it as a companion to the <em>Tree Calculus Book</em>.
      </p>
    </section>
    <section id="fundamental">
      <h2>
        Fundamental combinators
      </h2>
      <p>
        Tree calculus functions and values could be defined purely in terms of <code>Δ</code>, but some judicious naming helps make programming in trlisp more
        ergonomic. &nbsp;Let&apos;s <a href="https://blosavio.github.io/trlisp/tree-calculus.definitions.html">define</a> a small number of fundamental
        functions, and along the way, we will see the &nbsp;application rules in action.
      </p>
      <h3 id="K-combinator">
        K combinator
      </h3>
      <p>
        We&apos;ll start with the simplest function, the <em>K combinator</em>: simple in what it does, simple in structure, and simple in its
        &nbsp;application.
      </p>
      <p>
        The K combinator always returns the first of its two arguments.
      </p>
      <pre><code>(K Δ (Δ Δ Δ)) ;; =&gt; Δ</code></pre>
      <p>
        Given trees <code>Δ</code> and <code>(Δ&nbsp;Δ&nbsp;Δ)</code>, <code>K</code> returns <code>Δ</code>, the first argument, and discards the second
        argument. Here, we are using &nbsp;concrete trees to demonstrate the behavior, but <code>K</code> works the same on any pair of trees.
      </p>
      <p>
        We&apos;ve already seen in our discussion of the tree calculus application &nbsp;rules that <a href="#apply-fork-leaf">Rule&nbsp;3</a> declares that a
        fork-leaf always returns the first of a pair of arguments. &nbsp;So we know that we can create a K&nbsp;combinator by adopting that fork-leaf
        &nbsp;pattern.
      </p>
      <p>
        Putting an expression that evaluates to a fork-leaf <code>(Δ&nbsp;Δ)</code> in the function position and applying it to two arguments, dispatches on
        &nbsp;application Rule&nbsp;3, and returns the first argument.
      </p>
      <pre><code>((Δ Δ) Δ (Δ Δ Δ)) ;; =&gt; Δ</code></pre>
      <p>
        <code>(Δ Δ)</code> evaluates to <code>K</code>, so this expression returns the exact same <code>Δ</code> as before.
      </p>
      <p>
        We&apos;ve demonstrated that once we&apos;ve composed a tree that triggers a &nbsp;particular tree calculus application rule, we can use that tree to
        get a &nbsp;wanted behavior.
      </p>
      <p>
        In fact, trlisp&apos;s <a href=
        "https://github.com/blosavio/trlisp/blob/0f9f83904ebf16b54e8429ab5559aebce6e00b77/src/tree_calculus/definitions.clj#L102">actual definition</a> shows
        us that <code>K</code> is simply defined as <code>(Δ&nbsp;Δ)</code>. Now that the symbol <code>K</code> is bound to a tree that creates a fork-leaf
        tree when given two arguments, we &nbsp;can easily re-use the semantic idea of <em>Take the first argument, discard the second argument</em> in any
        future expression.
      </p>
      <h3 id="D-combinator">
        D combinator
      </h3>
      <p>
        A step up in complexity is the <em>D combinator</em>. Applied to three arguments, it does this.
      </p>
      <pre><code>(D x y z) = ((y z) (x z))</code></pre>
      <p>
        This operation is useful when we want to replicate an element and/or when &nbsp;we want to swap the order of two items.
      </p>
      <p>
        We define D like so.
      </p>
      <pre><code>(Def D (Δ (Δ Δ) (Δ Δ Δ)))</code></pre>
      <p>
        We can visualize D like this.
      </p>
      <pre>  Δ
&nbsp;/ \
Δ   (K Δ)
|
Δ</pre>
      <p>
        To see D in action, we&apos;ll assign <code>x</code>, <code>y</code>, and <code>z</code> to concrete trees, any of which may occupy the function
        position.
      </p>
      <pre><code>(def x (K Δ))</code><br><code>(def y (K Δ))</code><br><code>(def z K)</code></pre>
      <p>
        These are merely small trees with no particular semantics. We use them &nbsp;only because it&apos;s straightforward to apply them by hand.
      </p>
      <p>
        Let&apos;s evaluate the sub-components.
      </p>
      <pre><code>(y z) ;; =&gt; Δ</code></pre>
      <p>
        because <code>(K&nbsp;Δ&nbsp;K)</code> yields <code>Δ</code> due to application of the K&nbsp;combinator.
      </p>
      <p>
        The second sub-component is identical to the first.
      </p>
      <pre><code>(x z) ;; =&gt; Δ</code></pre>
      <p>
        Now let&apos;s evaluate the value of <code>(y&nbsp;z)</code> applied to the value of <code>(x&nbsp;z)</code>.
      </p>
      <pre><code>(Δ Δ) ;; =&gt; K</code></pre>
      <p>
        Let&apos;s check that against evaluating the entire expression.
      </p>
      <pre><code>((y z) (x z)) ;; =&gt; K</code></pre>
      <p>
        Finally, let&apos;s invoke trlisp&apos;s definition of <code>D</code> with <code>x</code>, <code>y</code>, and <code>z</code> as arguments.
      </p>
      <pre><code>(D x y z) ;; =&gt; K</code></pre>
      <p>
        Notice that, as with the K&nbsp;combinator, the D&nbsp;combinator is a tree &nbsp;deliberately composed to invoke a particular application rule, in
        this &nbsp;instance, Rule&nbsp;4 for fork-stems. After a couple rounds of application, we &nbsp;obtain the following picture.
      </p>
      <pre><code>  Δ
&nbsp;/ \
Δ   y  ␣  z
|
x</code></pre>
      <p>
        Rule&nbsp;4 immediately declares this to evaluate to
      </p>
      <pre><code>((y z) (x z))</code></pre>
      <p>
        which is the D&nbsp;combinator&apos;s essence.
      </p>
      <p>
        If we work out that sequence of evaluations by hand, we notice that D &nbsp;applied to the first argument <code>x</code> always eventually evaluates to
      </p>
      <pre><code>(Δ (Δ x))</code></pre>
      <p>
        which looks like this.
      </p>
      <pre><code>Δ
|
Δ
|
x</code></pre>
      <p>
        It&apos;s convenient to short-cut those steps, so we designate a function <code>d</code> such that
      </p>
      <pre><code>((d x) y z) ;; =&gt; ((y z) (x z))</code></pre>
      <p>
        which is the same result produced by the D&nbsp;combinator.
      </p>
      <h3 id="S-combinator">
        S combinator
      </h3>
      <p>
        The D combinator, in addition to broadcasting the third argument, also &nbsp;swaps the order of the first two arguments. It just so happens that there
        is &nbsp;a combinator that broadcasts the third argument to the first two arguments &nbsp;while maintaining the order.
      </p>
      <p>
        The <em>S&nbsp;combinator</em> works like this
      </p>
      <pre><code>(S x y z) ;; =&gt; ((x z) (y z))</code></pre>
      <p>
        and can be <a href=
        "https://github.com/blosavio/trlisp/blob/0f9f83904ebf16b54e8429ab5559aebce6e00b77/src/tree_calculus/definitions.clj#L116-L117">defined</a> in terms of
        <code>d</code>, <code>D</code>, and&nbsp;<code>K</code>.
      </p>
      <p>
        We can test the definition by evaluating with arguments <code>x</code>, <code>y</code>, and <code>z</code> from the previous subsection.
      </p>
      <pre><code>(= (S x y z)
&nbsp;  ((x z) (y z))) ;; =&gt; true</code></pre>
      <p>
        The definition for S checks out.
      </p>
      <p>
        It may be a little surprising to learn that the S combinator plays almost &nbsp;no role in any upcoming definition. It&apos;s main utility is that with
        only <code>S</code> and <code>K</code> in hand, we know that tree calculus is <em>S‑K&nbsp;complete</em>, and therefore can compute any function
        whatsoever.
      </p>
      <h3 id="I-combinator">
        I combinator
      </h3>
      <p>
        Another crucial combinator is the <em>I&nbsp;combinator</em>, which returns exactly its single argument.
      </p>
      <pre><code>(I x) ;; =&gt; x</code></pre>
      <p>
        The I combinator could be defined in terms of <code>S</code> and <code>K</code>, but such a definition is extensionally equivalent to trlisp&apos;s
        simpler <a href=
        "https://github.com/blosavio/trlisp/blob/0f9f83904ebf16b54e8429ab5559aebce6e00b77/src/tree_calculus/definitions.clj#L105-L106">definition</a>,
      </p>
      <pre><code>(Δ K K)</code></pre>
      <p>
        which we can visualize like this.
      </p>
      <pre><code>  Δ
&nbsp;/ \
Δ   Δ
|   |
Δ   Δ</code></pre>
      <p>
        Note that the tree structure of I&nbsp;combinator is designed to invoke &nbsp;Rule&nbsp;4, applying fork-stems, then immediately thereafter applies
        &nbsp;Rule&nbsp;3, for fork-leaves to discard the second expression, returning &nbsp;exactly the argument. <strong>The structure encodes the
        behavior.</strong>
      </p>
      <p>
        The I&nbsp;combinator appears in many upcoming definitions, including a key &nbsp;role in defining Boolean entities.
      </p>
      <h3 id="Y-combinator">
        Y combinator
      </h3>
      <p>
        trlisp supports recursion via the fixpoint combinator, or <em>Y combinator</em>, used in many of the upcoming definitions. Y is awkward to use
        directly, &nbsp;but know that it exists and is available.
      </p>
      <h3>
        Other combinators
      </h3>
      <p>
        For completeness, trlisp also provides the <a href="https://blosavio.github.io/trlisp/tree-calculus.definitions.html#var-B">B</a>, <a href=
        "https://blosavio.github.io/trlisp/tree-calculus.definitions.html#var-C">C</a>, and <a href=
        "https://blosavio.github.io/trlisp/tree-calculus.definitions.html#var-W">W</a> combinators, but does not use them for any further purposes.
      </p>
      <p>
        It turns out that we wouldn&apos;t often directly use any of these combinators &nbsp;in day-to-day programming, but they are critical building blocks
        for the <a href="#base">functions</a> we <em>do</em> use.
      </p>
    </section>
    <section id="base">
      <h2>
        Base functions
      </h2>
      <p>
        In contrast to the <a href="#fundamental">fundamental combinators</a>, these functions might be useful for solving day-to-day problems. We should
        &nbsp;continually keep in mind that these functions are <a href=
        "https://github.com/blosavio/trlisp/blob/0f9f83904ebf16b54e8429ab5559aebce6e00b77/src/tree_calculus/definitions.clj#L129">defined</a> (almost) solely
        in terms of <code>Δ</code>, or other trees themselves defined with <code>Δ</code>, a key characteristic of tree calculus.
      </p>
      <h3 id="Boolean">
        Booleans
      </h3>
      <p>
        The two Boolean values behave like Church Booleans, with <code>True</code> returning the first of two arguments, and <code>False</code> returning the
        second of two arguments.
      </p>
      <pre><code>(True x y) ;; =&gt; x</code><br><code>(False x y) ;; =&gt; y</code></pre>
      <p>
        In addition to <code>Not</code>, trlisp provides the usual suspects: <code>And</code>, <code>Or</code>, <code>Implies</code>, and <code>Iff</code>.
        Here&apos;s just a sampling.
      </p>
      <pre><code>(And True (Not True)) ;; =&gt; False</code><br><code>(Or True False) ;; =&gt; True</code><br><code>(Iff False False) ;; =&gt; True</code><br><code>(Implies True False) ;; =&gt; False</code></pre>
      <p>
        See the <a href=
        "https://github.com/blosavio/trlisp/blob/0f9f83904ebf16b54e8429ab5559aebce6e00b77/test/tree_calculus/definitions_test.clj#L201-L236">testing
        namespace</a> for the complete truth tables.
      </p>
      <h3 id="arithmetic">
        Arithmetic
      </h3>
      <p>
        Similar to Church encoding, tree calculus represents natural numbers as &nbsp;repeated application of <code>K</code> such that number&nbsp;<em>n</em>
        is represented by <em>K<sup>n</sup>Δ</em>. Let&apos;s formulate the first few numbers and visualize them.
      </p>
      <table>
        <tr>
          <th>
            number
          </th>
          <th>
            tree
          </th>
          <th>
            visualization
          </th>
        </tr>
        <tr>
          <td>
            Zero
          </td>
          <td>
            <pre><code>Δ</code></pre>
          </td>
          <td>
            <pre><code>  Δ
</code></pre>
          </td>
        </tr>
        <tr>
          <td>
            One
          </td>
          <td>
            <pre><code>(K Δ)</code></pre>
          </td>
          <td>
            <pre><code>  Δ
&nbsp;/ \
Δ   Δ
</code></pre>
          </td>
        </tr>
        <tr>
          <td>
            Two
          </td>
          <td>
            <pre><code>(K (K Δ))</code></pre>
          </td>
          <td>
            <pre><code>  Δ
&nbsp;/ \
Δ   Δ
&nbsp;  / \
&nbsp; Δ   Δ
</code></pre>
          </td>
        </tr>
        <tr>
          <td>
            Three
          </td>
          <td>
            <pre><code>(K (K (K Δ)))</code></pre>
          </td>
          <td>
            <pre><code>  Δ
&nbsp;/ \
Δ   Δ
&nbsp;  / \
&nbsp; Δ   Δ
&nbsp;    / \
&nbsp;   Δ   Δ
</code></pre>
          </td>
        </tr>
        <tr>
          <td>
            And so on.
          </td>
        </tr>
      </table>
      <p>
        It&apos;s obnoxious to have to bash those out on the keyboard, so the <a href="https://blosavio.github.io/trlisp/tree-calculus.utilities.html">utility
        namespace</a> provides a pair of functions to interconvert integers and trees.
      </p>
      <pre><code>(nat-&gt;tree 2) ;; =&gt; [[] [[] []]]</code><br><br><code>(tree-&gt;nat (K (K Δ))) ;; =&gt; 2</code></pre>
      <p>
        All the following functions require us to supply the arguments as trees, &nbsp;not plain integers, so we&apos;ll often be relying on
        <code>nat-&gt;tree</code> and <code>tree-&gt;nat</code>. Here&apos;s why.
      </p>
      <p>
        trlisp provides the classic <em>increment</em> and <em>decrement</em> functions. Let&apos;s calculate what arrives after integer <em>One</em>.
      </p>
      <pre><code>(Successor [[] []]) ;; =&gt; [[] [[] []]]</code></pre>
      <p>
        That&apos;s…not excellent. The argument and return value are a pile of &nbsp;brackets.
      </p>
      <p>
        Let&apos;s insert the <code>nat-&gt;tree</code> and <code>tree-&gt;nat</code> utilities to convert the argument and return value trees back to plain
        &nbsp;integers.
      </p>
      <p>
        Clojure provides a handy threading macro so we don&apos;t have to read the &nbsp;expression from the inside out.
      </p>
      <pre><code>(-&gt; 2
&nbsp;   nat-&gt;tree
&nbsp;   Successor
&nbsp;   tree-&gt;nat) ;; =&gt; 3</code></pre>
      <p>
        That&apos;s a tad better.
      </p>
      <p>
        We feed plain integer <code>2</code> into <code>nat-&gt;tree</code>, which converts it into the tree representation
        <code>[[]&nbsp;[[]&nbsp;[]]]</code>. Then, we feed that tree into <code>Successor</code>, which does its calculation, returning yet another tree.
        Finally, we feed &nbsp;that tree into <code>tree-&gt;nat</code>, which hands us an understandable integer&nbsp;<code>3</code>, which, since we paid
        attention in school, we have good reason to believe is &nbsp;the correct answer.
      </p>
      <p>
        trlisp also provides the companion function, <code>Predecessor</code>.
      </p>
      <pre><code>(-&gt; 5
&nbsp;   nat-&gt;tree
&nbsp;   Predecessor
&nbsp;   tree-&gt;nat) ;; =&gt; 4</code></pre>
      <p>
        trlisp provides functions for addition, subtraction, multiplication, and &nbsp;division.
      </p>
      <p>
        Let&apos;s demonstrate addition by first binding trees to <code>Two</code> and <code>Three</code>.
      </p>
      <pre><code>(def Two (nat-&gt;tree 2))</code><br><code>(def Three (nat-&gt;tree 3))</code></pre>
      <p>
        Then we evaluate the addition expression, feeding the result into <code>tree-&gt;nat</code> to convert the tree into a readable integer.
      </p>
      <pre><code>(-&gt; (Plus Two Three)
&nbsp;   tree-&gt;nat) ;; =&gt; 5</code></pre>
      <p>
        Yup. Adding two to three results in five.
      </p>
      <p>
        Beware: the <code>Divide</code> function is doubly-recursive, and performs poorly.
      </p>
      <h3 id="structures">
        Lists and tuples
      </h3>
      <p>
        trlisp implements <em>2-tuples</em> with <code>Pair</code> and provides accessor functions <code>First</code> and <code>Second</code>.
      </p>
      <pre><code>(Pair Two Three) ;; =&gt; [[[] [[] []]] [[] [[] [[] []]]]]</code><br><br><code>(-&gt; (Second (Pair Two Three))
&nbsp;   tree-&gt;nat) ;; =&gt; 3</code></pre>
      <p>
        Tree calculus lists are serial forks with the values <em>v<sub>n</sub></em> in the left branches.
      </p>
      <pre><code>  Δ
&nbsp;/ \
v0  Δ
&nbsp;  / \
&nbsp; v1  Δ
&nbsp;    / \
&nbsp;   v2  Δ
&nbsp;      / \
&nbsp;     …   …</code></pre>
      <p>
        trlisp provides <code>List</code> to quickly construct a list from its arguments.
      </p>
      <p>
        <code>Bite</code> constructs an <em>8-tuple</em> of bits, while <code>String</code> constructs strings from Bites representing the characters&apos;
        <span class="small-caps">ascii</span> byte encodings.
      </p>
      <p>
        Append to trlisp lists with <code>Append</code> and reverse a trlisp list with <code>Reverse</code>.
      </p>
      <h3 id="folding">
        Mapping and folding
      </h3>
      <p>
        Tree calculus defines functions for operating on elements of a list. &nbsp;trlisp implements a <em>mapping</em> function that applies a function to
        every element of a list a returns &nbsp;a new list with updated values.
      </p>
      <p>
        Let&apos;s construct a list of integers.
      </p>
      <pre><code>(def Our-List (List (nat-&gt;tree 2) (nat-&gt;tree 3) (nat-&gt;tree 4)))</code></pre>
      <p>
        Pretend we&apos;d like to have a new list containing each of those integers &nbsp;incremented by one. The <code>Successor</code> function makes a solid
        choice for incrementing.
      </p>
      <p>
        The function signature for mapping is
      </p>
      <pre><code>(Map <em>function list</em>)</code></pre>
      <p>
        Our expression will look like this.
      </p>
      <pre><code>(Map Successor Our-List)</code></pre>
      <p>
        <code>Map</code> returns a tree, which is difficult to decipher. So we&apos;ll use <code>List-&gt;seq</code> to convert the tree calculus list into a
        Clojure sequence of tree integers, &nbsp;and then Clojure&apos;s <code>map</code> with <code>tree-&gt;nat</code> to convert each tree integer into a
        plain integer.
      </p>
      <pre><code>(-&gt;&gt; (Map Successor Our-List)
&nbsp;    List-&gt;seq
&nbsp;    (map tree-&gt;nat)) ;; =&gt; (3 4 5)</code></pre>
      <p>
        Excellent. <code>Map</code> incremented three integers contained in our list.
      </p>
      <p>
        trlisp also implements tree calculus&apos; <em>folding</em> operations. The function signature for left-folding is
      </p>
      <pre><code>(Fold-Left <em>function init list</em>)</code></pre>
      <p>
        Let&apos;s compose those arguments. We&apos;ll use <code>Plus</code> since it&apos;s easy to eyeball it&apos;s effects. We&apos;ll assign a tree to an
        initial &nbsp;value.
      </p>
      <pre><code>(def Our-Init (nat-&gt;tree 1))</code></pre>
      <p>
        We&apos;ll use the same list of integers from earlier, <code>Our-List</code>.
      </p>
      <p>
        Now, we invoke the fold, and convert the result to an integer we can &nbsp;recognize.
      </p>
      <pre><code>(-&gt; (Fold-Left Plus Our-Init Our-List)
&nbsp;   tree-&gt;nat) ;; =&gt; 10</code></pre>
      <p>
        <code>Fold-Left</code> adds one to two, then that result to three, then that result to four, &nbsp;yielding ten.
      </p>
      <p>
        <code>Fold-Right</code> works analogously.
      </p>
      <h3 id="tags">
        Tagging
      </h3>
      <p>
        Tree calculus defines a system for tagging values, removing tags, &nbsp;reading the tags, retrieving the values, and for applying tagged functions
        &nbsp;to tagged values.
      </p>
      <p>
        Based on those tagging utilities, tree calculus defines a simple type &nbsp;system that tags a tree with a type, which can later be checked during
        &nbsp;application.
      </p>
      <p>
        trlisp implements the both the tagging and type-check systems, but they&apos;re &nbsp;not user-friendly, nor comprehensive, so we won&apos;t discuss
        them further. See &nbsp;the <a href=
        "https://github.com/blosavio/trlisp/blob/085c0b686b18e7dd3de2c4d59f8dc81791613782/test/tree_calculus/definitions_test.clj#L496-L535">testing
        namespace</a> for basic usage.
      </p>
      <h3 id="tree-analysis">
        Tree analysis
      </h3>
      <p>
        Tree calculus&apos; tentpole feature is the ability of one function to inspect &nbsp;another function directly, without quoting. This feature,
        <em>reflection</em>, is enabled by the fact that all entities, functions and values, are &nbsp;composed of the same stuff: trees.
      </p>
      <p>
        trlisp implements several functions in this category, such as measuring &nbsp;the size of a function, determining the equality of two functions, and
        running &nbsp;generic queries on the branching structure.
      </p>
      <p>
        For example, we can use the <code>Size</code> function to measure the size of the K combinator.
      </p>
      <pre><code>(-&gt; (Size K)
&nbsp;   tree-&gt;nat) ;; =&gt; 2</code></pre>
      <p>
        Yes, the K combinator is composed of two nodes as we expect. And we didn&apos;t &nbsp;need to operate on some quoted expression preceding a compilation
        step. <code>Size</code> operated directory on K&apos;s defined value.
      </p>
      <p>
        We could also ask if two functions are equal. Let&apos;s see if the K &nbsp;combinator is equal to one leaf node descendant from another node.
      </p>
      <pre><code>(Equal? K (Δ Δ)) ;; =&gt; True</code></pre>
      <p>
        Yes, the two trees are equal as we expect. <code>Equal?</code> dived straight into the internal structures of <code>K</code> and
        <code>(Δ&nbsp;Δ)</code> to do its job.
      </p>
      <p>
        Tree calculus&apos; <em>triage</em> defines a basic system for testing for leaves, stems, or forks. With triage &nbsp;in hand, tree calculus goes on to
        define <em>pattern matching</em>. As I understand it, pattern-matching answers the following question.
      </p>
      <p>
        Given tree&nbsp;<em>A</em>,
      </p>
      <pre><code>  Δ
&nbsp;/ \
Δ   foo</code></pre>
      <p>
        and some target component of&nbsp;<em>A</em>,
      </p>
      <pre><code>foo</code></pre>
      <p>
        and test tree&nbsp;<em>B</em>,
      </p>
      <pre><code>  Δ
&nbsp;/ \
Δ   baz</code></pre>
      <p>
        What is the <em>thing</em> located in tree&nbsp;<em>B</em> at the same location as <code>foo</code> in tree&nbsp;<em>A</em>? The answer is
        <code>baz</code>.
      </p>
      <p>
        trlisp implements both triage and pattern matching, and they pass some <a href=
        "https://github.com/blosavio/trlisp/blob/085c0b686b18e7dd3de2c4d59f8dc81791613782/test/tree_calculus/definitions_test.clj#L592-L661">rudimentary unit
        tests</a>, but I am not confident the tests are correct nor sufficient.
      </p>
      <p>
        At any rate, we <em>should</em> be impressed with the fact that all these functions, sophisticated as they &nbsp;are, are <a href=
        "https://github.com/blosavio/trlisp/blob/0f9f83904ebf16b54e8429ab5559aebce6e00b77/src/tree_calculus/definitions.clj#L399">defined</a> in terms of the
        basic node operator.
      </p>
      <h3 id="evaluators">
        Self evaluators
      </h3>
      <p>
        Tree calculus demonstrates its ability for programs to act on themselves &nbsp;by defining four self-evaluators. trlisp implements all four:
        <em>Branch-first</em>, <em>Root</em>, <em>Root-and-branch</em>, and <em>Root-first</em> evaluate their arguments with different strategies for
        inspecting the &nbsp;interior structure of their arguments.
      </p>
      <p>
        See the <a href=
        "https://github.com/blosavio/trlisp/blob/0f9f83904ebf16b54e8429ab5559aebce6e00b77/test/tree_calculus/definitions_test.clj#L698-L884">testing
        namespace</a> for example usage.
      </p>
    </section>
    <section id="comments">
      <h2>
        Comments
      </h2>
      <p>
        Two of tree calculus&apos; ideas caught my attention.
      </p>
      <ol>
        <li>
          <strong>One operator plus six application rules</strong>
          <p>
            Tree calculus&apos; minimalism hints that it could be implemented in &nbsp;constrained environments. Almost certainly a 6502&nbsp;microprocessor,
            probably on a &nbsp;punch card machine, but maybe even a marble machine by someone really clever.
          </p>
          <p>
            Tree calculus is so concise, we can evaluate the expressions by hand. I &nbsp;wrote trlisp so that I could evaluate tree calculus expressions to
            <a href=
            "https://github.com/blosavio/trlisp/blob/a40862ed5d0036ca84d77a1e922a68ed0eb007ff/test/tree_calculus/exercising_test.clj#L100-L174">check</a> my
            pencil and paper work, and to feel what it&apos;s like to program with. In &nbsp;that regard, trlisp provides that. While working through the
            book&apos;s examples, &nbsp;trlisp can quickly validate an expression that takes multiple sheets of paper.
          </p>
          <p>
            On the other hand, it is immediately apparent that trlisp is in no way a &nbsp;practical, general-purpose programming language. The inputs and
            return values &nbsp;are trees, not plainly understandable integers and strings, and bugs are &nbsp;tedious to track down. trlisp kinda grafts tree
            calculus&apos; evaluation model onto &nbsp;an existing language. What advantages could an entirely new programming &nbsp;language based on tree
            calculus offer?
          </p>
          <p>
            Instead of adding tree calculus itself to another programming language, &nbsp;adapting some of tree calculus&apos; <em>features</em> to an existing
            programming language could be beneficial.
          </p>
        </li>
        <li>
          <strong>Decomposable functions</strong>
          <p>
            Check this out.
          </p>
          <pre><code>(Second D) ;; =&gt; (K Δ)</code></pre>
          <p>
            We just reached into the D&nbsp;combinator, a function-like thing, and pulled &nbsp;out its second element, i.e., the right child. We didn&apos;t
            resort to quoting or a &nbsp;macro. The tree that implements the D&nbsp;combinator is, at any moment, available &nbsp;for inspection. There is no
            separate notion of a function&apos;s definition that is &nbsp;distinct from the thing that executes the task.
          </p>
          <p>
            Having such reflective functions, i.e., direct access to a function&apos;s &nbsp;definition, suggests some interesting possibilities. Being able to
            inspect, &nbsp;analyze, optimize, modify, and borrow pieces from a function could enable tasks &nbsp;that are not currently easy, or even possible.
            In recent memory, I have at &nbsp;least <a href="https://blosavio.github.io/speculoos/speculoos.utility.html#var-defpred">one utility</a> that
            would have been faster and more elegant to write if I had run-time &nbsp;(not compile-time) access to the function&apos;s definition.
          </p>
          <p>
            Could it be generally useful to be able to define a new function by &nbsp;modifying an existing one?
          </p>
          <pre><code>;; pseudo-code</code><br><code>(def my-new-fn (assoc old-fn ...))</code></pre>
          <p>
            When we speak of &apos;first-class functions&apos;, we generally mean that functions may be passed as values and returned from other functions. But
            what if we could dive into a function and do something useful with its contents? Wouldn&apos;t reflective functions truly be
            &apos;first-class&apos;?
          </p>
          <p>
            We probably wouldn&apos;t use it often, but it could be a nice tool in the toolbox. Something akin to writing a Lisp macro when a regular function
            won&apos;t suffice. I can more easily imagine how this particular tree calculus feature might be added to an existing language.
          </p>
        </li>
      </ol>
    </section>
    <section id="references">
      <h2>
        References
      </h2>
      <h3>
        Tree calculus references
      </h3>
      <ul>
        <li>
          <p>
            Barry Jay&apos;s <a href="https://github.com/barry-jay-personal/tree-calculus/blob/master/tree_book.pdf">Reflective Programs in Tree Calculus</a>
            (pdf, 2021)
          </p>
          <p>
            trlisp&apos;s primary reference. See also the <a href="https://github.com/barry-jay-personal/tree-calculus">other materials</a> in Jay&apos;s
            GitHub page, particularly the Coq proofs.
          </p>
        </li>
        <li>
          <p>
            Johannes Bader&apos;s <a href="https://treecalcul.us/">Tree Calculus</a> page
          </p>
          <p>
            Tree calculus advocacy, examples, and interactive demos. Note: The application rules are different than here, but the systems are equivalent. Bader
            also wrote a nice <a href="https://olydis.medium.com/a-visual-introduction-to-tree-calculus-2f4a34ceffc2">introduction blog post</a>.
          </p>
        </li>
        <li>
          <p>
            Timur Latypoff&apos;s <a href="https://latypoff.com/tree-calculus-visualized/">Tree calculus, visualized</a>
          </p>
        </li>
        <li>
          <p>
            James Eversole&apos;s <a href="https://git.eversole.co/James/tricu">tricu</a>
          </p>
          <p>
            Tree calculus implemented in Haskell.
          </p>
        </li>
      </ul>
      <h3>
        Other references
      </h3>
      <ul>
        <li>
          <p>
            Palmer, Filardo, &amp; Wu <a href="https://www.cs.swarthmore.edu/~zpalmer/publications/intensional-functions.pdf">Intensional functions</a> (pdf,
            2024)
          </p>
          <p>
            Discusses how intensional functions offer non-application operations, such as comparison, serialization, hashing, etc.
          </p>
        </li>
      </ul>
    </section>
    <section id="glossary">
      <h2>
        Glossary
      </h2>
      <dl>
        <dt id="apply">
          apply
        </dt>
        <dd>
          <p>
            Consume two trees, yielding a single tree according to the tree calculus rules. In trlisp, we <code>apply</code> by evaluating a list with a
            function (a <a href="#tree">tree</a>) at the head and an argument (also a tree) at its tail.
          </p>
          <p>
            An instance of applying one tree to another is an <em>application</em>. trlisp provides six distinct applications, selected by the branching
            pattern of the tree in the function position.
          </p>
        </dd>
        <dt id="argument">
          argument
        </dt>
        <dd>
          <p>
            A tree appearing in the tail of a list to be evaluated. The argument is consumed in the course of applying the function.
          </p>
          <p>
            Note: All trees may serve as an argument, even trees that are semantically functions, such as <code>Inc</code>. In other words, trees are
            first-class functions and may be passed as values and manipulated.
          </p>
        </dd>
        <dt id="define">
          define
        </dt>
        <dd>
          <p>
            Bind a name (a Clojure symbol) to tree. That name evaluates to the original tree wherever it appears in an expression.
          </p>
        </dd>
        <dt id="fork">
          fork
        </dt>
        <dd>
          <p>
            A node with exactly two children.
          </p>
        </dd>
        <dt id="function">
          function
        </dt>
        <dd>
          <p>
            A tree appearing at the head of a list to be evaluated. The function&apos;s branching pattern determines which application rule to invoke.
          </p>
          <p>
            Note: All trees are potentially functions, i.e., any tree may competently invoke one of the applications.
          </p>
        </dd>
        <dt id="leaf">
          leaf
        </dt>
        <dd>
          <p>
            A node with exactly zero children.
          </p>
        </dd>
        <dt id="node">
          node
        </dt>
        <dd>
          <p>
            The basic unit of an unlabelled, binary tree. May be either a leaf, stem, or fork.
          </p>
        </dd>
        <dt id="stem">
          stem
        </dt>
        <dd>
          <p>
            A node with exactly one child.
          </p>
        </dd>
        <dt id="tree">
          tree
        </dt>
        <dd>
          <p>
            An arrangement of only <a href="#leaf">leafs</a>, <a href="#stem">stems</a>, and <a href="#fork">forks.</a>
          </p>
        </dd>
      </dl>
    </section><br>
    <h2>
      License
    </h2>
    <p></p>
    <p>
      This program and the accompanying materials are made available under the terms of the <a href="https://opensource.org/license/MIT">MIT License</a>.
    </p>
    <p></p>
    <p id="page-footer">
      Copyright © 2024–2025 Brad Losavio.<br>
      Compiled by <a href="https://github.com/blosavio/readmoi">ReadMoi</a> on 2025 June 17.<span id="uuid"><br>
      24d7622e-9828-4fe1-831b-4a4314e293e0</span>
    </p>
  </body>
</html>
