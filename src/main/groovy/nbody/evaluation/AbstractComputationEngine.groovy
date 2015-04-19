package nbody.evaluation

import nbody.model.Particle

/**
 * Created by vivek on 15/04/15.
 */
abstract class AbstractComputationEngine {
    final protected Util util

    AbstractComputationEngine(Util util) {
        this.util = util
    }

    public abstract compute(List<Particle> allParticles, final double deltaT, int numberOfCalculationIterations);
}