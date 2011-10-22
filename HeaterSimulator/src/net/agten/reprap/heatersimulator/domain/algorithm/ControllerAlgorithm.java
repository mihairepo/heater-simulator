package net.agten.reprap.heatersimulator.domain.algorithm;

public interface ControllerAlgorithm {
	short nextValue(short curAdc);
	void setTargetAdc(short targetAdc);
}
