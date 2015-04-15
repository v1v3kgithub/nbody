package nbody.evaluation

import nbody.model.Particle

/**
 * Created by vivek on 15/04/15.
 */
class SerialComputationEngine extends AbstractComputationEngine{

    SerialComputationEngine(Closure createCombinationOfParticles,
                            Closure evaluateForceBetweenParticles,
                            Closure updatePositionAndVelocity) {
        super(createCombinationOfParticles,evaluateForceBetweenParticles,updatePositionAndVelocity)
    }

    public compute(List<Particle> allParticles, final double deltaT, int numberOfCalculationIterations) {
        for (i in 0..numberOfCalculationIterations) {
            List<Tuple2<Particle,Particle>> pairsOfParticles = createCombinationOfParticles.call(allParticles)
            pairsOfParticles.each {
                actionToPerformBetweenParticles.call(it)
            }
            actionToPerformAfterAllIntraParticleEvaluation.call(allParticles,deltaT)
        }
    }
}
