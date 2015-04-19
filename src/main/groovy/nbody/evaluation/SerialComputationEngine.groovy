package nbody.evaluation

import nbody.model.Particle

/**
 * Created by vivek on 15/04/15.
 */
class SerialComputationEngine extends AbstractComputationEngine{
    SerialComputationEngine(Util util) {
        super(util)
    }

    public compute(List<Particle> allParticles, final double deltaT, int numberOfCalculationIterations) {
        for (i in 0..numberOfCalculationIterations) {
            List<Tuple2<Particle,Particle>> pairsOfParticles = util.createPairOfParticles(allParticles)
            pairsOfParticles.each {
                util.calculateForceBetweenParticles(it)
            }
            util.updateLocationAndVelocity(allParticles,deltaT)
        }
    }
}
