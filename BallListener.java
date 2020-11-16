
public interface BallListener {
	public void onBallTick();
	public void onBallCollidingCalculated();
	public void onWallHitted(Ball ball);
	public void onBallStopped(Ball ball);
}
