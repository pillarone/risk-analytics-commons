package org.pillarone.riskanalytics.domain.utils.marker;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.pillarone.riskanalytics.core.components.IComponentMarker;

/**
 * @author stefan.kunz (at) intuitive-collaboration (dot) com
 */
public class ComposedMarkerKey {

    private IComponentMarker marker1;
    private IComponentMarker marker2;
    private IComponentMarker marker3;
    private Integer number;

    public ComposedMarkerKey(IComponentMarker firstMarker, IComponentMarker secondMarker) {
        marker1 = firstMarker;
        marker2 = secondMarker;
        number = 2;
    }

    public ComposedMarkerKey(IComponentMarker firstMarker, IComponentMarker secondMarker, IComponentMarker thirdMarker) {
        marker1 = firstMarker;
        marker2 = secondMarker;
        marker3 = thirdMarker;
        number = 3;
    }

    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(marker1);
        hashCodeBuilder.append(marker2);
        if (number == 3) {
            hashCodeBuilder.append(marker3);
        }
        return hashCodeBuilder.toHashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof ComposedMarkerKey) {
            if (number == 3) {
                return ((ComposedMarkerKey) obj).getMarker1().equals(marker1)
                        && ((ComposedMarkerKey) obj).getMarker2().equals(marker2)
                        && ((ComposedMarkerKey) obj).getMarker3().equals(marker3)
                        && ((ComposedMarkerKey) obj).getNumber().equals(number);
            }
            else {
                return ((ComposedMarkerKey) obj).getMarker1().equals(marker1)
                        && ((ComposedMarkerKey) obj).getMarker2().equals(marker2)
                        && ((ComposedMarkerKey) obj).getNumber().equals(number);
            }
        } else {
            return false;
        }
    }

    public IComponentMarker getMarker1() {
        return marker1;
    }

    public IComponentMarker getMarker2() {
        return marker2;
    }

    public IComponentMarker getMarker3() {
        return marker3;
    }

    public Integer getNumber() {
        return number;
    }
}
