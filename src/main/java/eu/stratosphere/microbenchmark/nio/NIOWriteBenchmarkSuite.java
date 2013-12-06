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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * A suite of NIO buffer benchmarks to execute.
 * 
 * @author Alexander Alexandrov (alexander.alexandrov@tu-berlin.de)
 */
@RunWith(Suite.class)
// @formatter:off
@SuiteClasses({ 
	NIOWriteBenchmarkSuite.SequentialManagedSingle.class ,
	NIOWriteBenchmarkSuite.SequentialAllocateSingle.class,
	NIOWriteBenchmarkSuite.SequentialDirectSingle.class  ,
	NIOWriteBenchmarkSuite.SequentialManagedBulk.class   ,
	NIOWriteBenchmarkSuite.SequentialAllocateBulk.class  ,
	NIOWriteBenchmarkSuite.SequentialDirectBulk.class 
})
// @formatter:on
public class NIOWriteBenchmarkSuite {

	private static final int TOTAL_SIZE = 1024 * 1024 * 50;

	public static class SequentialManagedSingle extends NIOWriteBenchmark {
		public SequentialManagedSingle() {
			super(TOTAL_SIZE, BufferAllocationStrategy.MANAGED, ChannelWritingStrategy.SINGLE);
		}
	}

	public static class SequentialAllocateSingle extends NIOWriteBenchmark {
		public SequentialAllocateSingle() {
			super(TOTAL_SIZE, BufferAllocationStrategy.ALLOCATE, ChannelWritingStrategy.SINGLE);
		}
	}

	public static class SequentialDirectSingle extends NIOWriteBenchmark {
		public SequentialDirectSingle() {
			super(TOTAL_SIZE, BufferAllocationStrategy.DIRECT, ChannelWritingStrategy.SINGLE);
		}
	}

	public static class SequentialManagedBulk extends NIOWriteBenchmark {
		public SequentialManagedBulk() {
			super(TOTAL_SIZE, BufferAllocationStrategy.MANAGED, ChannelWritingStrategy.BULK);
		}
	}

	public static class SequentialAllocateBulk extends NIOWriteBenchmark {
		public SequentialAllocateBulk() {
			super(TOTAL_SIZE, BufferAllocationStrategy.ALLOCATE, ChannelWritingStrategy.BULK);
		}
	}

	public static class SequentialDirectBulk extends NIOWriteBenchmark {
		public SequentialDirectBulk() {
			super(TOTAL_SIZE, BufferAllocationStrategy.DIRECT, ChannelWritingStrategy.BULK);
		}
	}
}
