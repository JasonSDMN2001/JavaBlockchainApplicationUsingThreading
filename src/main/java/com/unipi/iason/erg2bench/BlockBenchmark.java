package com.unipi.iason.erg2bench;

import com.unipi.iason.erg2v1.Block;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2)
@Warmup(iterations = 1)
@Measurement(iterations = 4)
public class BlockBenchmark {

    @Param({"10000000"})
    private int N;

    private Block block;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BlockBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Setup
    public void setup() {
        // Initialize your Block object here
        block = new Block("0", createData(), System.currentTimeMillis());
    }

    @Benchmark
    public void mineBlockBenchmark(Blackhole bh) {
        // Benchmark the mineBlock method
        block.mineBlock(N);
        bh.consume(block);
    }

    private List<Product> createData() {
        List<Product> data = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            Product product = new Product.Builder(i)
                    .productCode("1234")
                    .title("Title" + i)
                    .timestamp(System.currentTimeMillis())
                    .price(10.0)
                    .description("Description" + i)
                    .category("Category" + i)
                    .previousProductId(i - 1)
                    .build();
            data.add(product);
        }
        return data;
    }
}