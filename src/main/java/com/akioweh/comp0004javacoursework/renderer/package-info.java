/**
 * Complements the servlets as Views in the MVC pattern.
 * Provides some rendering logic that would be too cumbersome to stick into JSP.
 * <p>
 * Renderers are provided as singletons as they provide an effectively static interface.
 * (But their methods are not static so that they all implement the same interface.)
 */
package com.akioweh.comp0004javacoursework.renderer;
