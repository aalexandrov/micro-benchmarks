package eu.stratosphere.microbenchmark.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkOptionsSystemProperties;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkHistoryChart;
import com.carrotsearch.junitbenchmarks.annotation.LabelType;

import eu.stratosphere.microbenchmark.nio.NIOWriteBenchmark.BufferAllocationStrategy;
import eu.stratosphere.microbenchmark.nio.NIOWriteBenchmark.ChannelWritingStrategy;

@BenchmarkHistoryChart(filePrefix = "nio-write", labelWith = LabelType.CUSTOM_KEY, maxRuns = 20)
@BenchmarkOptions(benchmarkRounds = NIOWriteBenchmark.BENCHMARK_ROUNDS, warmupRounds = NIOWriteBenchmark.WARMUP_ROUNDS)
public class NIOWriteBenchmarkPersisted extends AbstractBenchmark {

	private static final Pattern PATTERN = Pattern.compile("^(managed|allocate|direct)-(single|bulk)-(\\d+)MB$");

	private final NIOWriteBenchmark adaptee;

	public NIOWriteBenchmarkPersisted() {
		if (System.getProperty(BenchmarkOptionsSystemProperties.CUSTOMKEY_PROPERTY) == null) {
			throw new RuntimeException(String.format("Missing required key property '%s'", BenchmarkOptionsSystemProperties.CUSTOMKEY_PROPERTY));
		}

		Matcher m = PATTERN.matcher(System.getProperty(BenchmarkOptionsSystemProperties.CUSTOMKEY_PROPERTY));
		if (!m.matches()) {
			throw new RuntimeException(String.format("Required key property '%s' has bad format", BenchmarkOptionsSystemProperties.CUSTOMKEY_PROPERTY));
		}

		BufferAllocationStrategy bufferAllocationStrategy = BufferAllocationStrategy.valueOf(m.group(1).toUpperCase());
		ChannelWritingStrategy channelWritingStrategy = ChannelWritingStrategy.valueOf(m.group(2).toUpperCase());
		int totalSize = 1024 * 1024 * Integer.parseInt(m.group(3).toUpperCase());

		this.adaptee = new NIOWriteBenchmark(totalSize, bufferAllocationStrategy, channelWritingStrategy);
	}

	@Before
	public void resetBuffers() {
		this.adaptee.resetBuffers();
	}

	@Test
	public void writeToFile0004K() throws FileNotFoundException, IOException {
		this.adaptee.writeToFile0004K();
	}

	@Test
	public void writeToFile0008K() throws FileNotFoundException, IOException {
		this.adaptee.writeToFile0008K();
	}

	@Test
	public void writeToFile0016K() throws FileNotFoundException, IOException {
		this.adaptee.writeToFile0016K();
	}

	@Test
	public void writeToFile0064K() throws FileNotFoundException, IOException {
		this.adaptee.writeToFile0064K();
	}

	@Test
	public void writeToFile0256K() throws FileNotFoundException, IOException {
		this.adaptee.writeToFile0256K();
	}

	@Test
	public void writeToFile1024K() throws FileNotFoundException, IOException {
		this.adaptee.writeToFile1024K();
	}
}