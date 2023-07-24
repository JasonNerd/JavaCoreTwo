---
title: "JavaCoreTwo-stream-(1)"
date: 2023-07-14T15:20:57+08:00
draft: false
tags: ["stream", "Java"]
categories: ["JavaCoreTwo"]
twemoji: true
lightgallery: true
---

与集合相比，**流提供了一种可以让我们在更高的概念级别上指定计算任务的数据视图**。通过使用流，我们可以**说明想要完成什么任务，而不是说明如何去实现它**。我们将操作的调度留给具体实现去解决。例如，假设我们想要计算某个属性的平均值，那么我们就可以指定数据源和该属性，然后，流库就可以对计算进行优化，例如，使用多线程来计算总和与个数，并将结果合并。

## 1. 集合与流
### api
```Java
// java.util.streamStream<T>
// 产生一个流，其中包含当前流中满足p的所有元素
Stream<T> filter(Predicate<? super T> p);
// 产生当前流中元素的数量。这是一个终止操作。
long count();
/********************/
// java.util.Collection<E>
default Stream<E> stream();
default Stream<E> parallelStream();
产生当前集合中所有元素的顺序流或并行流
```
读取txt文件并转为静态的List
```java
public static String[] txt2List(String txtPathStr) throws IOException {
    Path txtPath = Paths.get(txtPathStr);
    String contents = Files.readString(txtPath);    // may throw IOException
    return contents.split("\\PL+");    // 以非字母作为分隔符
}
```
此处需要注意, 在IDEA中运行时, 需要指明源码所在目录(例如code), 假如txt文件放在与code同级的txt目录, 那么传入的路径参数应为: `txt/xxx.txt`

### demo
统计txt文件里长度大于len的单词个数
```java
public static long countWordsFor(List<String> wordList, int len){
    long res = 0;
    for(String w: wordList){
        if(w.length() > len) res++;
    }
    return res;
}
public static long countWordsStream(List<String> wordList, int len){
    return wordList.stream().filter(w->w.length()>len).count();
}
public static long countWordsParallelStream(List<String> wordList, int len){
    return wordList.parallelStream().filter(w->w.length()>len).count();
}

// test func
countWords(List.of(txt2List("txt/textA.txt")), 10);
```
**流遵循做什么而非怎么做的原则**。在流的示例中，我们描述了需要做什么:获取长单词，并对它们计数。我们没有指定该操作应该以什么顺序或者在哪个线程中执行。相比之下，在循环遍历的示例中要确切地指定计算应该如何工作，因此也就丧失了进行优化的机会。流表面上看起来和集合很类似，都可以让我们转换和获取数据。但是，它们之间存在着显著的差异:
1.**流并不存储其元素**。这些元素可能存储在底层的集合中，或者是按需生成的
2.**流的操作不会修改其数据源**。例如，fiter 方法不会从流中移除元素，而是会生成一个新的流，其中不包含被过滤掉的元素。
3.**流的操作是尽可能惰性执行的**。这意味着直至需要其结果时，操作才会执行。例如如果我们只想查找前5个长单词而不是所有长单词，那么 filter 方法就会在匹配到第 5个单词后停止过滤。**因此，我们甚至可以操作无限流**。

这个工作流是操作流时的典型流程。我们建立了一个包含三个阶段的操作管道:
1.**创建一个流**。
2.指定**将初始流转换为其他流的中间操作**，可能包含多个步骤。
3.**应用终止操作，从而产生结果。这个操作会强制执行之前的惰性操作。从此之后，这个流就再也不能用了。**

## 2. 流的创建
### api
```java
// java.util.streamStream<T>

// 产生一个元素为给定值的流
static <T> Stream<T> of(T... values);

// 产生一个不包含任何元素的流
static <T> Stream<T> empty()

// 产生一个无限流，它的值是通过反复调用函数s而构建的
static<T> Stream<T> generate(Supplier<T> s)

// 产生一个无限流，它的元素包含 seed, 在 seed 上调用f 产生的值, 在前一个元素上调用f产生的值, 等等
// 第一个方法会产生一个无限流，而第二个方法的流会在碰到第一个不满足hasNext谓词的元素时终止
static <> Stream<T> iterate(T seed，UnaryOperator<T> f)
static <T> Stream<T> iterate(T seed, Predicate<? super T> hasNext, UnaryOperator<T> f)

// 如果t为null，返回一个空流，否则返回包含 t的流
static <T>Stream<T> ofNullable(T t)
```
为了程序演示方便, 编写一个函数 "打印流" 的前几个元素:
```java
public static <T> void showStream(String title, Stream<T> streamT, int size){
    List<T> res = streamT.limit(size).toList();
    System.out.print(title+": ");
    for (T re : res){
        System.out.print(re);
        System.out.print(", ");
    }
    System.out.println("... ...");
}
public static <T> void showStream(String title, Stream<T> streamT){
    showStream(title, streamT, 10);
}
```
该函数通过 limit 获取了流的前一部分元素(作为新流), 接着转为 list(终止), 然后进行打印.
### demo
```java
public static void streamGenTest(){
    // 1. of 方法
    try {
        List<String> wordList = CountWord.txt2List("txtPathStr");
        showStream("wordList", Stream.of(wordList));
    } catch (IOException e) {}
    Stream<String> songs = Stream.of("Star", "Monk", "Rock", "Pop");
    showStream("songs", songs);
    showStream("empty", Stream.empty());
    // 2. generate
    Stream<String> echos = Stream.generate(()->"echo");
    showStream("echos", echos);
    Stream<Double> doubleStream = Stream.generate(Math::random);
    showStream("doubleStream", doubleStream, 12);
    // 3. iterate
    Stream<BigInteger> integerStream = Stream.iterate(BigInteger.ONE,
            n->n.compareTo(BigInteger.valueOf(1000000)) < 0,
            n->n.add(BigInteger.ONE));
    showStream("integerStream", integerStream);
    // ... more info, look up to the book
}
```
## 3. 流的转换
### api
```java
Stream<T> filter(Predicate<? super T> predicate)产生一个流，它包含当前流中所有满足谓词条件的元素<R>Stream<R> map(Function<? super T,? extends R> mapper)
产生一个流，它包含将 mapper 应用于当前流中所有元素所产生的结果<R> Stream<R> flatMap(Function<? super T,? extends Stream<? extends R>> mapper)产生一个流，它是通过将 mapper 应用于当前流中所有元素所产生的结果连接到一起而获得的。(注意，这里的每个结果都是一个流。)
```
流的转换会产生一个新的流，它的元素派生自另一个流中的元素。我们已经看到了filter 转换会产生一个新流，它的元素与某种条件相匹配, filter的引元是`Predicate<T>`，即从T到 boolean 的函数

通常，我们**想要按照某种方式来转换流中的值，此时，可以使用 `map` 方法并传递执行该转换的函数**。例如，我们可以像下面这样将所有单词都转换为小写:
`Stream<String> lowercaseWords = wordsstream().map(String::toLowerCase);`
这里，我们使用的是**带有方法引用的 map**，但是，通常我们可以**使用lambda 表达式来代替**
`Stream<String> firstLetters = words,stream(),map(s -> s.substring(0，1));`
上面的语句所产生的流中包含了所有单词的首字母。
**在使用 map 时，会有一个函数应用到每个元素上，并且其结果是包含了应用该函数后所产生的所有结果的流**。

现在，假设我们有一个函数，它返回的不是一个值，而是一个包含众多值的流。下面的示例展示的方法会将字符串转换为字符串流，即一个个的编码点:
```java
public static Stream<String> codePoints(String str){
    ArrayList<String> arrayList = new ArrayList<>();
    int i = 0;
    while (i < str.length()){
        int j = str.offsetByCodePoints(i, 1);
        arrayList.add(str.substring(i, j));
        i = j;
    }
    return arrayList.stream();
}
```
这个方法可以正确地处理需要用两个 char 值来表示的 Unicode字符，因为本来就应该这样处理。但是，我们不用再次纠结其细节。

假设我们将codePoints 方法映射到一个字符串流上, 那么会得到一个包含流的流，为了将其摊平为单个流，可以使用 flatMap 方法而不是 map 方法, 除了 map, filter, flatMap 还有如下的其他方法:
```java
// 产生一个流，其中包含了当前流中最初的 maxsize个元素。
Stream<T>limit(long maxSize);

// 产生一个流，它的元素是当前流中除了前n个元素之外的所有元素 
Stream<T> skip(long n)

// 产生一个流，它的元素是当前流中所有满足谓词条件的元素
Stream<T> takeWhile(Predicate<? super T> predicate);

// 产生一个流，它的元素是当前流中排除谓词条件的元素后的所有元素
Stream<T> dropwhile(Predicate<? super T> predicate);

// 产生一个流，它的元素是 a的元素后面跟着 b的元素
static <T> Stream<T> concat(Stream<? extends T> a, Stream<? extends T> b);

// 产生一个流，包含当前流中所有不同的元素
Stream<T> distinct()

// 产生一个流，它的元素是当前流中的所有元素按照顺序排列的。第一个方法要求元素是实现了Comparable的类的实例。
Stream<T> sorted()
Stream<T> sorted(Comparator<? super T> comparator)

// 产生一个流，它与当前流中的元素相同，在获取其中每个元素时，会将其传递给 action。
Stream<T> peek(Consumer<? super T> action)
```
为了方便测试, 这里编写一个函数, 它直接返回一个 `Stream<String>` 以供测试:
```java
public static Stream<String> wordStream(){
    String txtPathStr = "txt/textA.txt";
    try{
        return Stream.of(CountWord.txt2List(txtPathStr));
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
```
### demo
```java
public static void test(){
    // 1. map: 转为全部大写
    showStream("Uppercase word list", wordStream().map(String::toUpperCase));
    // 2. map: 取首字母
    showStream("First letter", wordStream().map(w-> w.substring(0, 1)));
    // 3. map: 首字母大写
    showStream("Capitalized word list", wordStream().map(w->
            w.substring(0, 1).toUpperCase()+w.substring(1)));
    // 4. flatMap
    showStream("Flat letter stream", wordStream().flatMap(StreamTransfer::codePoints));
    // 5. filter: 取出包含数字的部分
    showStream("take the owl", codePoints("aoef9q83ur9wq8asudc").filter("0123456789"::contains));
}
```
## 4 Optional 类型
`Optional<T>`对象是一种**包装器对象**，要么包装了类型T的对象，要么没有包装任何对象对于第一种情况，我们称这种值是存在的。**`Optional<T>`类型被当作一种更安全的方式，用来替代类型T的引用**，这种引用要么引用某个对象，要么为 Null. 但是，它只有在正确使用的情况下才会更安全.
### 4.1 Optional 的创建
可以通过 of(), ofNullable(), empty() 来创建一个 Optional 对象:
```java
// 产生一个具有给定值的Optional。如果 value 为null，那么第一个方法会抛出一个NullPointerException 异常，而第二个方法会产生一个空Optional
static <T> Optional<T> of(T value)
static <T> Optional<T> ofNullable(T value)

// 产生一个空Optional
static <T>Optional<T> empty()
```
例如, 求倒数:
```java
public static Optional<Double> inverse(double d){
    return d == 0 ? Optional.empty() : Optional.of(1/d);
}
```
求平方根:
```java
public static Optional<Double> squareRoot(double d){
    return d < 0 ? Optional.empty() : Optional.of(Math.sqrt(d));
}
```
#### 使用 flatMap
考虑对一个 double 变量连续的运用 inverse, squareRoot 两个方法, 然而 inverse 的返回值是 `Optional<Double>` 不是 Double, 因此无法使用 .inverse().squareRoot(). 而 flatMap 解决了这个问题, 在前面我们知道 flatMap 可以将流的流展平为单层次的流, 这里也十分类似, 它将 `Optional<T>` 展平为 T:
```java
double d = 3.14;
Optional<Double> res = inverse(d)
        .flatMap(OpCreate::squareRoot)
        .flatMap(OpCreate::inverse);
System.out.println(res);
```
很明显，如果有更多可以产生 optional值的方法或lambda 表达式，那么就可以重复此过程。你可以**直接将对 flatMap 的调用链接起来，从而构建由这些步骤构成的管道，只有所有步骤都成功，该管道才会成功**

### 4.2 Optional 的使用
有效地使用 optional的关键是要使用这样的方法:它在值不存在的情况下会产生一个可替代物，而只有在值存在的情况下才会使用这个值。

**orElse**
如下为获取 Optional 包装类中的对象的几个方法
```java
// 产生这个Optional的值，或者在该Optional 为空时，产生other
T orElse(T other)
// 产生这个Optional的值，或者在该Optional为空时，产生调用other 的结果
T orElseGet(Supplier<? extends T> other)
// 产生这个 Optional 的值，或者在该 Optional 为空时，抛出调用 exceptionSupplier 的结果
<X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier)
```

**ifPresent**
使用可选值的策略是只有在其存在的情况下才消费该值, 例如ifPresent 方法会接受一个函数。如果可选值存在，那么它会被传递给该函数。否则，不会发生任何事情。
```java
// 如果该 Optional 不为空，就将它的值传递给 action
void ifPresent(Consumer<? super T> action)
// 如果该optional不为空，就将它的值传递给action，否则调用emptyAction
void ifPresentOrElse(Consumer<? super T> action,Runnable emptyAction) 
```

**map, filter**
可以通过 map 修改 Optional 中的 T 的对象, 通过 filter 过滤 Optional 中的 T 的对象. 
例如: `Optional<String> transformed = optionalString.map(String::toUpperCase);`, 如果optionalString为空，那么transformed也为空. 另一个例子，我们将一个结果添加到列表中，如果它存在的话:`optionalValue.map(results::add);`

类似地，可以使用 filter 方法来只处理那些在转换它之前或之后满足某种特定属性的optional值。如果不满足该属性，那么管道会产生空的结果:
`Optional<String> transformed = optionalString.filter(s ->slength()>=8)map(String::toUpperCase);`
你也可以用or 方法将空 ptional 替换为一个可替代的ptional。
```java
// 产生一个Optional，如果当前的 streams.optional 的值存在，那么所产生的 Optional的值是通过将给定的函数应用于当前的 Optional的值而得到的;否则，产生一个空的 Optional。
<U> Optional<U> map(Function<? super T,? extends U> mapper)
// 产生一个optional，如果当前的 optional的值满足给定的谓词条件，那么所产生的Optional的值就是当前 streams.optional 的值;否则，产生一个空 Optional。
Optional<T> filter(Predicate<? super T> predicate)
// 如果当前 Optional不为空，则产生当前的 Optional;否则由 supplier 产生一个Optional
Optional<T> or(Supplier<?extends Optional<? extends T>> supplier) 
```
### 4.3 使用 Optional 的建议
如果没有正确地使用Optional值，那么相比以往得到"某物或nul"的方式，你并没有得到任何好处: get 方法会在 streams.optional 值存在的情况下获得其中包装的元素，或者在不存在的情况下抛出一个 NoSuchElementException 异常。因此
```java
Optional<T> optionalValue = ...
optionalValue.get().someMethod();
```
并不比下面的方式更安全
```java
T value =....value.someMethod();
```
isPresent 方法会报告某个 `Optional<T>` 对象是否具有值。但是
```java
if (optionalValueisPresent()) optionalValue.get().someMethod();
```
并不比下面的方式更容易处理:
```java
if (value != null) value.someMethod();
```
以下是一些正确使用 Optional 的建议:
* Optional类型的变量永远都不应该为 null
* 不要使用optional类型的域。因为其代价是额外多出来一个对象。在类的内部，使用null表示缺失的域更易于操作
* 不要在集合中放置 Optional 对象，并且不要将它们用作 map 的键。应该直接收集其中的值

### 4.4 
stream方法会将一个`streams.optional<T>`对象转换为一个具有0个或1个元素的`Sream<T>`对象, 请考虑下面这样一个例子

假设我们有一个用户ID流和方法`Optional<User> lookup(String id)`, 我们无法保证 ID 流里每个 ID 都能在数据库中查询到相应的用户, 这些ID是无效的, 如何健壮地进行处理呢?
一方面我们可以过滤掉无效ID, 然后将 get 方法应用于剩余的ID: 
```java
Stream<String> ids=...;
Stream<User> users = ids.map(Users::lookup).filter(Optional::isPresent).map(Optional::get);
```
但是这样就需要使用我们之前警告过要慎用的 isPresent 和 get 方法。下面的调用显得更优雅:
```java
Stream<User> users = ids.map(Users::lookup).flatMap(Optional::stream);
```
每一个对 stream的调用都会返回一个具有0个或1个元素的流。flatMap 方法将这些流组合到一起, 此时不存在的用户将不存在于结果流.

