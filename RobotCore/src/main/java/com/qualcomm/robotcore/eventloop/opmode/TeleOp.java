package com.qualcomm.robotcore.eventloop.opmode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Op Mode classes must be annotated with either Autonomous or TeleOp in order to be displayed in the dropdown box
 * of available op modes.
 *
 * The @Target meta-annotation indicates that @TeleOp is intended to annotate a TYPE (i.e., class)
 * The @Retention meta-annotation is needed to make the @TeleOp annotation accessible at run time.
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TeleOp {
    String name();
    String group() default "default";
}
