/***********************************************************************************************************************
 *
 * Copyright (C) 2010 by the Stratosphere project (http://stratosphere.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 **********************************************************************************************************************/
package eu.stratosphere.microbenchmark.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.junit.Before;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;

/**
 * @author Alexander Alexandrov (alexander.alexandrov@tu-berlin.de)
 */
@BenchmarkOptions(benchmarkRounds = NIOWriteBenchmark.BENCHMARK_ROUNDS, warmupRounds = NIOWriteBenchmark.WARMUP_ROUNDS)
public class NIOWriteBenchmark extends AbstractBenchmark {

	protected static enum BufferAllocationStrategy {
		MANAGED, ALLOCATE, DIRECT
	}

	protected static enum ChannelWritingStrategy {
		SINGLE, BULK
	}

	protected static enum BufferSize {
		SIZE_4096(4096), SIZE_8192(8192), SIZE_16384(16384), SIZE_65536(65536), SIZE_262144(262144), SIZE_1048576(1048576);

		private int value;

		private BufferSize(int value) {
			this.value = value;
		}
	}

	/**
	 * Number of benchmark rounds per test.
	 */
	protected static final int BENCHMARK_ROUNDS = 11;

	/**
	 * Number of benchmark warmup rounds for the benchmark per test.
	 */
	protected static final int WARMUP_ROUNDS = 1;

	/**
	 * Width of the STDOUT reporting lines in.
	 */
	protected static final int LINE_LENGTH = 80;

	/**
	 * Total size of the files to be written in each test (in bytes).
	 */
	private final int totalSize;

	/**
	 * Allocation strategy for the buffers.
	 */
	private final BufferAllocationStrategy bufferAllocationStrategy;

	/**
	 * Channel writing for the buffers.
	 */
	private final ChannelWritingStrategy channelWritingStrategy;

	/**
	 * Prefix of the created files.
	 */
	private final String filePrefix;

	/**
	 * Buffer pool.
	 */
	private final ByteBuffer[][] buffers = new ByteBuffer[BufferSize.values().length][];

	public NIOWriteBenchmark(int totalSize, BufferAllocationStrategy bufferAllocationStrategy, ChannelWritingStrategy channelWritingStrategy) {
		this(totalSize, bufferAllocationStrategy, channelWritingStrategy, System.getProperty("java.io.tmpdir"));
	}

	public NIOWriteBenchmark(int totalSize, BufferAllocationStrategy bufferAllocationStrategy, ChannelWritingStrategy channelWritingStrategy, String ioDir) {
		// init parameter properties
		this.totalSize = totalSize;
		this.bufferAllocationStrategy = bufferAllocationStrategy;
		this.channelWritingStrategy = channelWritingStrategy;
		this.filePrefix = String.format("%s/nio_bm.%s-%s", ioDir, this.bufferAllocationStrategy.name(), this.channelWritingStrategy.name());

		// allocate the different buffers 
		for (BufferSize bs : BufferSize.values()) {
			ByteBuffer[] bb = new ByteBuffer[this.totalSize / bs.value];
			for (int i = 0; i < bb.length; i++) {
				if (this.bufferAllocationStrategy == BufferAllocationStrategy.ALLOCATE) {
					bb[i] = ByteBuffer.wrap(new byte[bs.value]);
				} else if (this.bufferAllocationStrategy == BufferAllocationStrategy.MANAGED) {
					bb[i] = ByteBuffer.allocate(bs.value);
				} else if (this.bufferAllocationStrategy == BufferAllocationStrategy.DIRECT) {
					bb[i] = ByteBuffer.allocateDirect(bs.value);
				}
			}
			this.buffers[bs.ordinal()] = bb;
		}

		String name = String.format("[%s, %s]", this.bufferAllocationStrategy, this.channelWritingStrategy);
		String prefix = new String(new char[(int) Math.ceil((LINE_LENGTH - (name.length() + 2)) / 2.0)]).replace("\0", "~");
		String suffix = new String(new char[(int) Math.floor((LINE_LENGTH - (name.length() + 2)) / 2.0)]).replace("\0", "~");
		System.out.println(prefix + " " + name + " " + suffix);
	}

	@Before
	public void resetBuffers() {
		// allocate the different buffers 
		for (BufferSize bs : BufferSize.values()) {
			ByteBuffer[] bb = this.buffers[bs.ordinal()];
			for (int i = 0; i < bb.length; i++) {
				bb[i].clear();
			}
		}
	}

	@Test
	public void writeToFile0004K() throws FileNotFoundException, IOException {
		writeToFile(BufferSize.SIZE_4096);
	}

	@Test
	public void writeToFile0008K() throws FileNotFoundException, IOException {
		writeToFile(BufferSize.SIZE_8192);
	}

	@Test
	public void writeToFile0016K() throws FileNotFoundException, IOException {
		writeToFile(BufferSize.SIZE_16384);
	}

	@Test
	public void writeToFile0064K() throws FileNotFoundException, IOException {
		writeToFile(BufferSize.SIZE_65536);
	}

	@Test
	public void writeToFile0256K() throws FileNotFoundException, IOException {
		writeToFile(BufferSize.SIZE_262144);
	}

	@Test
	public void writeToFile1024K() throws FileNotFoundException, IOException {
		writeToFile(BufferSize.SIZE_1048576);
	}

	private void writeToFile(BufferSize bufferSize) throws IOException {

		RandomAccessFile file = new RandomAccessFile(String.format("%s.%08d.tmp", this.filePrefix, bufferSize.value).toString(), "rwd");
		FileChannel channel = file.getChannel();
		channel.truncate(0);

		ByteBuffer[] buffers = this.buffers[bufferSize.ordinal()];

		try {
			if (this.channelWritingStrategy == ChannelWritingStrategy.SINGLE) {
				for (int i = 0; i < buffers.length; i++) {
					channel.write(buffers[i]);
				}
			}
			if (this.channelWritingStrategy == ChannelWritingStrategy.BULK) {
				int bytesRemaining = this.totalSize;
				int currFirstBuffer = 0;
				do {
					bytesRemaining -= channel.write(buffers, currFirstBuffer, buffers.length - currFirstBuffer);
					currFirstBuffer = (this.totalSize - bytesRemaining) / bufferSize.value;
				} while (bytesRemaining > 0);
			}
		} finally {
			channel.close();
			file.close();
		}
	}
}
