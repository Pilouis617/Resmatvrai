package spring.sio.resmat.config;

import com.samskivert.mustache.Mustache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Réglages de Mustache :
 * - une valeur absente ou nulle s'affiche comme une chaîne vide au lieu de provoquer une erreur ;
 * - une chaîne vide ou un zéro sont considérés comme « faux » dans une section.
 */
@Configuration
public class MustacheConfig {

    @Bean
    public Mustache.Compiler mustacheCompiler(Mustache.TemplateLoader templateLoader) {
        return Mustache.compiler().withLoader(templateLoader).defaultValue("")
                .emptyStringIsFalse(true)
                .zeroIsFalse(true);
    }
}
