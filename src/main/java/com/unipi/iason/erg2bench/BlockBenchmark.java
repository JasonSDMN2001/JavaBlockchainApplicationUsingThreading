package com.unipi.iason.erg2bench;

import com.unipi.iason.Product;
import com.unipi.iason.erg2v1.Block;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2)
@Warmup(iterations = 1)
@Measurement(iterations = 4)
public class BlockBenchmark {
    public static final long timeStamp = new Date().getTime();

    @Param({"5"})
    private int N;

    private Block block;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BlockBenchmark.class.getSimpleName())
                .threads(8)
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Setup
    public void setup() {
        // Initialize your Block object here with a single Product
        List<Product> data = new ArrayList<>();
        Product product = new Product.Builder(1)
                .productCode("1234")
                .title("Title1")
                .timestamp(timeStamp)
                .price(10.0)
                .description("Description1")
                .category("Category1")
                .previousProductId(0)
                .build();
        data.add(product);
        block = new Block("0", data, timeStamp);
    }

    @Benchmark
    public void mineBlockBenchmark(Blackhole bh) {
        // Benchmark the mineBlock method with different prefixes
        for (int i = 0; i < N; i++) {
            block.mineBlock(i);
            bh.consume(block);
        }
    }
}