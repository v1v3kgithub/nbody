package nbody.evaluation

import nbody.model.Particle

/**
 * Created by vivek on 15/04/15.
 */
abstract class AbstractComputationEngine {
    final protected Closure createCombinationOfParticles;
    final protected Closure actionToPerformBetweenParticles;
    final protected Closure actionToPerformAfterAllIntraParticleEvaluation;

    AbstractComputationEngine(Closure createCombinationOfParticles,
                                     Closure actionToPerformBetweenParticles,
                                     Closure actionToPerformAfterAllIntraParticleEvaluation) {
        this.createCombinationOfParticles = createCombinationOfParticles;
        this.actionToPerformBetweenParticles = actionToPerformBetweenParticles;
        this.actionToPerformAfterAllIntraParticleEvaluation = actionToPerformAfterAllIntraParticleEvaluation;
    }

    public abstract compute(List<Particle> allParticles, final double deltaT, int numberOfCalculationIterations);
}