package nbody

import nbody.evaluation.AbstractComputationEngine
import nbody.evaluation.ParallelComputationEngine
import nbody.evaluation.SerialComputationEngine
import nbody.evaluation.Util
import nbody.model.Particle

import javax.vecmath.Point3d
import java.util.Random
import javax.vecmath.Vector3d

/**
 * Created by v2 on 13/04/15.
 */

double randomWithRange(min,max) {
    double range = Math.abs(max - min)
    return (Math.random() * range) + (min <= max ? min : max)
}

List<Particle> createRandomParticles(numberofParticles) {
    def particles = new ArrayList<Particle>();
    (0..<numberofParticles).each {
        particles[it] = new Particle("" + it,randomWithRange(100,200),
                new Point3d(randomWithRange(100,200),randomWithRange(200,100),randomWithRange(200,100)));
    }
    return particles
}


Util util = new Util()
SerialComputationEngine serialComputationEngine =
        new SerialComputationEngine(util.&createPairOfParticles,
                util.&calculateForceBetweenParticles,util.&updateLocationAndVelocity);
ParallelComputationEngine parallelComputationEngine =
        new ParallelComputationEngine(util.&createPairOfParticles,
                util.&calculateForceBetweenParticles,
                util.&updateLocationAndVelocity)

/*
def particles = createRandomParticles(100)
println particles[1]
serialComputationEngine.compute(particles,0.1,1000)
println particles[1]

parallelComputationEngine.compute(particles,0.1,1000)
println particles[1]
*/

double computeTime(AbstractComputationEngine computationEngine,double deltaT,int numberOfIterations,List<Particle> particles) {
    double start= System.currentTimeMillis()
    computationEngine.compute(particles,deltaT,numberOfIterations)
    double end= System.currentTimeMillis()
    return end - start
}
void performace(AbstractComputationEngine serialComputationEngine,
                AbstractComputationEngine parallelComputationEngine,
                int minNumberOfParticles,
                int maxNumberOfParticles,
                int numberOfParticleIncrementSize = 10,
                int minNumberOfIterrations,
                int maxNumerOfIterrations,
                int numberIterationsIncrementSize = 10,
                double deltaT) {
    println "\n"
    println "Min Number of Particles $minNumberOfParticles"
    println "Max Number of Particles $maxNumberOfParticles"
    println "Min Number of Iterations $minNumberOfIterrations"
    println "Max Number of Iterations $maxNumerOfIterrations"
    println "NumberOfParticles,NumberOfIteratioons,SerialComputeTime(ms),ParellemComputeTime(ms),%Improvement"
    minNumberOfParticles.step(maxNumberOfParticles,numberOfParticleIncrementSize) {
        int numberOfParticles = it
        List<Particle> particles = createRandomParticles(it)
        minNumberOfIterrations.step(maxNumerOfIterrations,numberIterationsIncrementSize) {
            int numberOfIterations = it
            double serialTime = computeTime(serialComputationEngine,deltaT,numberOfIterations,particles)
            double parallelTime = computeTime(parallelComputationEngine,deltaT,numberOfIterations,particles)
            println "$numberOfParticles,$numberOfIterations,$serialTime,$parallelTime,${((serialTime-parallelTime)/serialTime)*100} "
        }
    }
}
println "Mutiple calculation runs....\n\n"
performace(serialComputationEngine,parallelComputationEngine,200,1000,50,100,101,10,0.1)

parallelComputationEngine.executorService.shutdown()

