package pl.polsl.gk.tabletopSimulator.particles;
import pl.polsl.gk.tabletopSimulator.entities.Entity;

import java.util.List;

public interface IEmitter {

    void cleanup();

    Particle getBaseParticle();

    List<Entity> getParticles();

}
