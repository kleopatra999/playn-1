/**
 * Copyright 2010-2015 The PlayN Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package playn.scene;

import playn.core.Canvas;
import playn.core.Graphics;

/**
 * Simplifies the process of displaying a {@link Canvas} which is updated after its initial
 * creation. When modifying the canvas, one must call {@link #begin} to obtain a reference to the
 * canvas, do the desired rendering, then call {@link #end} to upload the modified image data to
 * the GPU for display by this layer.
 */
public class CanvasLayer extends ImageLayer {

  private final Graphics gfx;
  private Canvas canvas;

  /**
   * Creates a canvas layer with a backing canvas of size {@code width x height} (in display
   * units). This layer will display nothing until a {@link #begin}/{@link #end} pair is used to
   * render something to its backing canvas.
   */
  public CanvasLayer (Graphics gfx, float width, float height) {
    this.gfx = gfx;
    resize(width, height);
  }

  /**
   * Creates a canvas layer with the supplied backing canvas. The canvas will immediately be
   * uploaded to the GPU for display.
   */
  public CanvasLayer (Graphics gfx, Canvas canvas) {
    this.gfx = gfx;
    this.canvas = canvas;
    setTexture(gfx.createTexture(canvas.image));
  }

  /**
   * Resizes the canvas that is displayed by this layer.
   *
   * <p>Note: this throws away the old canvas and creates a new blank canvas with the desired size.
   * Thus this should immediately be followed by a {@link #begin}/{@link #end} pair which updates
   * the contents of the new canvas. Until then, it will display the old image data.
   */
  public void resize (float width, float height) {
    canvas = gfx.createCanvas(width, height);
  }

  /** Starts a drawing operation on this layer's backing canvas. Thus must be follwed by a call to
    * {@link #end} when the drawing is complete. */
  public Canvas begin () {
    return canvas;
  }

  /** Informs this layer that a drawing operation has just completed. The backing canvas image data
    * is uploaded to the GPU. */
  public void end () {
    // TODO: if our texture is the right size, just update it
    setTexture(gfx.createTexture(canvas.image));
  }

  @Override public float width() {
    return (width < 0) ? canvas.width : width;
  }

  @Override public float height() {
    return (height < 0) ? canvas.height : height;
  }
}
