package worm.esort.thread;
public class WormLock {
		private boolean shouldPause = false;

		public boolean isShouldPause() {
			return shouldPause;
		}

		public void setShouldPause(boolean shouldPause) {
			this.shouldPause = shouldPause;
		}

	}
