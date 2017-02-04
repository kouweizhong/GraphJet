/**
 * Copyright 2016 Twitter. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twitter.graphjet.bipartite;

import com.twitter.graphjet.bipartite.api.EdgeIterator;
import com.twitter.graphjet.bipartite.segment.LeftIndexedBipartiteGraphSegment;

/**
 * This iterator provides reverse chronological access over edges where the edges can be spread across segments.
 */
public class MultiSegmentReverseIterator<T extends LeftIndexedBipartiteGraphSegment> extends MultiSegmentIterator<T> {
  /**
   * This constructor mirror the one in it's super-class to reuse common code.
   *
   * @param multiSegmentBipartiteGraph  is the underlying {@link LeftIndexedMultiSegmentBipartiteGraph}
   * @param segmentEdgeAccessor   is the accessor for the segments
   */
  public MultiSegmentReverseIterator(
      LeftIndexedMultiSegmentBipartiteGraph<T> multiSegmentBipartiteGraph,
      SegmentEdgeAccessor<T> segmentEdgeAccessor) {
    super(multiSegmentBipartiteGraph, segmentEdgeAccessor);
  }


  @Override
  public EdgeIterator resetForNode(long inputNode) {
    super.resetForNode(inputNode);
    super.currentSegmentId = liveSegmentId;
    initializeCurrentSegmentIterator();
    return this;
  }

  /**
   * This finds segments in reverse chronological order.
   * Returns false if it cannot find a non-empty next segment for the node
   */
  @Override
  protected boolean findNextSegmentForNode() {
    while ((currentSegmentIterator == null || !currentSegmentIterator.hasNext())
      && (currentSegmentId > oldestSegmentId)) {
      currentSegmentIterator = segmentEdgeAccessor.getNodeEdges(--currentSegmentId, node);
    }
    return currentSegmentIterator != null && currentSegmentIterator.hasNext();
  }
}
