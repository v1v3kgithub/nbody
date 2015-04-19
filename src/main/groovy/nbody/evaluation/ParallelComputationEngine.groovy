package nbody.evaluation

import nbody.model.Particle

import java.util.concurrent.CountDownLatch
import java.util.concurrent.CyclicBarrier
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by vivek on 15/04/15.
 */
class ParallelComputationEngine extends AbstractComputationEngine {
    final ExecutorService executorService
    private final int numberOfCPUs = Runtime.runtime.availableProcessors()

    ParallelComputationEngine(Util util) {
        super(util)
        this.executorService = Executors.newFixedThreadPool(numberOfCPUs)
    }

    public compute(List<Particle> allParticles, final double deltaT, int numberOfCalculationIterations) {

        List<Tuple2<Particle, Particle>> particlePairs = util.createPairOfParticles(allParticles)

        int particlesPerCpu = Math.ceil(particlePairs.size() / numberOfCPUs) // Allowing for overflow
        if (particlesPerCpu < 5) {
            throw new IllegalStateException("Please use serial compute engine very less number of particles")
        }
        List<List<Tuple2<Particle, Particle>>> subListOfParticlePairs =
                particlePairs.collate(particlesPerCpu)


        CountDownLatch numberOfIterationsCounterLatch = new CountDownLatch(numberOfCalculationIterations)
        final CyclicBarrier barrier = new CyclicBarrier(
                subListOfParticlePairs.size(),
                new Runnable() {
                    // Action to perform at the end of each iteration
                    @Override
                    void run() {
                        util.updateLocationAndVelocity(allParticles, deltaT)
                        numberOfIterationsCounterLatch.countDown()
                    }
                }
        )

        subListOfParticlePairs.each{ List<Tuple2<Particle,Particle>> sublist ->
/*
            // This is very slow ???
            executorService.execute( new Runnable() {

                @Override
                void run() {
                    while (numberOfIterationsCounterLatch.count > 0) {
                        sublist.each {
                            util.calculateForceBetweenParticles(it)
                        }
                        barrier.await()
                    }
                }
            })
*/
          // This goes very fast
            def calculateForceBetweenParticleWorker = {
                while (numberOfIterationsCounterLatch.count > 0) {
                    sublist.each {
                        util.calculateForceBetweenParticles(it)
                    }
                    barrier.await()
                }
            } as Runnable
            executorService.execute(calculateForceBetweenParticleWorker)
        }
        numberOfIterationsCounterLatch.await()

    }
}
