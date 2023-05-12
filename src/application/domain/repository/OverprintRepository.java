package application.domain.repository;

public class OverprintRepository {

	/* nieuzywane */

	public Double paintPrice(Double priceOfPaint, Double quantity, Double[] additions) { //farba + dodatki
		double priceOfAdd = 0.0;
		for (Double add : additions) {
			priceOfAdd += add;
		}
		return priceOfPaint * quantity + priceOfAdd;
	}
	public Double extraPrice(Integer powerOfDeviceInWatt, Double timeInHours, Double onekWhPrice) { //prad np 180 Wat + czas
		double kWh = powerOfDeviceInWatt / 1000 * timeInHours;
		return kWh * onekWhPrice; //koszt korzystania z urządzenia przez x godzin
	}
	public Double secondaryPrice(Double matrixPrice, Double priceOfTheNet, Integer numOfColors) { //matryca + siatka. 1 matryca na 1 kolor
		return numOfColors * matrixPrice + priceOfTheNet;
	}

}
