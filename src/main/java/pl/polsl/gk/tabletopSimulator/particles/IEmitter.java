package pl.polsl.gk.tabletopSimulator.particles;

import pl.polsl.gk.tabletopSimulator.Game;
import java.util.List;

public interface IEmitter {

    void cleanup();

    Particle getBaseParticle();

    List<Game> getParticles();

}
