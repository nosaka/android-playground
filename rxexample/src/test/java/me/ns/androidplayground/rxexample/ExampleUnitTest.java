package me.ns.androidplayground.rxexample;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void test() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);

        Observable.range(0, 10)
                .doOnCompleted(() -> System.out.println("@"))
                .doOnCompleted(() -> System.out.println("★"))
                .subscribe(System.out::println, System.out::println, latch::countDown);
    }

    @Test
    public void test_map() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);

        Observable.range(0, 10)
                .doOnCompleted(() -> System.out.println("@"))
                .map(v -> 10 + v)
                .doOnCompleted(() -> System.out.println("★"))
                .subscribe(System.out::println, System.out::println, latch::countDown);
    }

    @Test
    public void test_flat_map() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);

        Observable.just(1, 2, 3)
                .doOnCompleted(() -> System.out.println("@"))
                .flatMap(i -> Observable.range(i, 3))
                .doOnCompleted(() -> System.out.println("★"))
                .subscribe(System.out::println, System.out::println, latch::countDown);
    }

    @Test
    public void test_skip() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);

        Observable.just(1, 2, 3)
                .doOnCompleted(() -> System.out.println("@"))
                .skip(3)
                .doOnCompleted(() -> System.out.println("★"))
                .subscribe(System.out::println, System.out::println, latch::countDown);
    }

    @Test
    public void test_take() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);

        Observable.just(1, 2, 3)
                .doOnCompleted(() -> System.out.println("@"))
                .take(2)
                .doOnCompleted(() -> System.out.println("★"))
                .subscribe(System.out::println, System.out::println, latch::countDown);
    }

    @Test
    public void test_reduce() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);

        Observable.range(5, 3)
                .doOnCompleted(() -> System.out.println("@"))
                .reduce((v1, v2) -> v1 + v2)
                .doOnCompleted(() -> System.out.println("★"))
                .subscribe(System.out::println, System.out::println, latch::countDown);
    }

    @Test
    public void test_scan() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);

        Observable.range(1, 3)
                .doOnCompleted(() -> System.out.println("@"))
                .scan((v1, v2) -> v1 + v2)
                .doOnCompleted(() -> System.out.println("★"))
                .subscribe(System.out::println, System.out::println, latch::countDown);
    }

    @Test
    public void test_collect() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);

        Observable.range(1, 3)
                .doOnCompleted(() -> System.out.println("@"))
                .collect(ArrayList::new, (list, integer) -> {
                    list.add(Integer.toString(integer));
                })
                .doOnCompleted(() -> System.out.println("★"))
                .subscribe(System.out::println, System.out::println, latch::countDown);

    }

    @Test
    public void test_merge() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);

        Observable<Integer> a = Observable.just(0);
        Observable<Integer> b = Observable.just(1, 2);
        Observable<Integer> c = Observable.just(3, 4, 5);
        Observable<Integer> d = Observable.just(6, 7, 8);
        Observable<Integer> e = Observable.just(9, 10);

        Observable<Integer> merge1 = Observable.merge(a, b);
        Observable<Integer> merge2 = Observable.merge(c, d);
        Observable<Integer> merge3 = Observable.merge(a, b, c, d, e);

        Observable.merge(merge1, merge2, merge3)
                .doOnCompleted(() -> System.out.println("@"))
                .doOnCompleted(() -> System.out.println("★"))
                .subscribe(System.out::println, System.out::println, latch::countDown);

    }

    @Test
    public void test_concat() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);

        Observable<Integer> a = Observable.just(0);
        Observable<Integer> b = Observable.just(1, 2);
        Observable<Integer> c = Observable.just(3, 4, 5);
        Observable<Integer> d = Observable.just(6, 7, 8);
        Observable<Integer> e = Observable.just(9, 10);

        Observable<Integer> merge1 = Observable.concat(a, b);
        Observable<Integer> merge2 = Observable.concat(c, d);
        Observable<Integer> merge3 = Observable.concat(a, b, c, d, e);

        Observable.concat(merge1, merge2, merge3)
                .doOnCompleted(() -> System.out.println("@"))
                .doOnCompleted(() -> System.out.println("★"))
                .subscribe(System.out::println, System.out::println, latch::countDown);

    }

    @Test
    public void test_zip() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);

        Observable<Integer> a = Observable.just(0, 1, 2);
        Observable<Integer> b = Observable.just(3, 4, 5);
        Observable<Integer> c = Observable.just(6, 7, 8, 9);

        Observable.zip(a, b, c,
                (v1, v2, v3) -> String.format(Locale.getDefault(), "%1$d#%2$d#%3$d", v1, v2, v3))
                .doOnCompleted(() -> System.out.println("@"))
                .doOnCompleted(() -> System.out.println("★"))
                .subscribe(System.out::println, System.out::println, latch::countDown);

    }

    @Test
    public void test_combineLatest() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);

        Observable<Integer> a = Observable.just(0, 1, 2, 3, 4);
        Observable<Integer> b = Observable.just(5, 6, 7, 8, 9);

        Observable.combineLatest(a, b,
                (v1, v2) -> String.format(Locale.getDefault(), "%1$d#%2$d", v1, v2))
                .doOnCompleted(() -> System.out.println("@"))
                .doOnCompleted(() -> System.out.println("★"))
                .subscribe(System.out::println, System.out::println, latch::countDown);

    }

    @Test
    public void test_groupBy() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);

        Observable.range(1, 10)
                .doOnCompleted(() -> System.out.println("@"))
                .groupBy(v1 -> v1 % 2)
                .doOnCompleted(() -> System.out.println("★"))
                .flatMap(Observable::toList)
                .subscribe(System.out::println, System.out::println, latch::countDown);

    }

    @Test
    public void test_distinct() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);

        class Human {
            int id;
            String name;
            int age;

            public Human(int id, String name, int age) {
                this.id = id;
                this.name = name;
                this.age = age;
            }

            @Override
            public String toString() {
                return name;
            }
        }
        List<Human> humens = new ArrayList<Human>();
        humens.add(new Human(1, "太郎", 30));
        humens.add(new Human(2, "花子", 20));
        humens.add(new Human(3, "次郎", 26));
        humens.add(new Human(4, "三郎", 23));

        humens.add(new Human(2, "クローン人間花子", 18)); // 美しく生まれ変わるためにクローンとして転生した花子

        Observable.from(humens)
                .doOnCompleted(() -> System.out.println("@"))
                .distinct(human -> human.id)
                .doOnCompleted(() -> System.out.println("★"))
                .subscribe(System.out::println, System.out::println, latch::countDown);

    }

    @Test
    public void test_window() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);

        Observable.range(1, 10)
                .doOnCompleted(() -> System.out.println("@"))
                .window(3)
                .doOnCompleted(() -> System.out.println("★"))
                .flatMap(Observable::toList)
                .subscribe(System.out::println, System.out::println, latch::countDown);

    }

    @Test
    public void test_buffer() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);

        Observable.range(1, 10)
                .doOnCompleted(() -> System.out.println("@"))
                .buffer(3)
                .doOnCompleted(() -> System.out.println("★"))
                .subscribe(System.out::println, System.out::println, latch::countDown);

    }

    @Test
    public void test_repeat() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);

        Observable.range(1, 3)
                .doOnCompleted(() -> System.out.println("@"))
                .repeat(3)
                .doOnCompleted(() -> System.out.println("★"))
                .subscribe(System.out::println, System.out::println, latch::countDown);

    }

    @Test
    public void test_timeout() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);

        Observable.just(0, 1, 2, 3, 4, 5, 6, 7)
                .doOnNext(integer -> {
                    try {
                        Thread.sleep(TimeUnit.SECONDS.toMillis(2));
                    } catch (InterruptedException ignore) {
                    }
                })
                .timeout(1, TimeUnit.SECONDS)
                .doOnCompleted(() -> System.out.println("@"))
                .doOnCompleted(() -> System.out.println("★"))
                .subscribe(System.out::println, System.out::println, latch::countDown);

    }


}