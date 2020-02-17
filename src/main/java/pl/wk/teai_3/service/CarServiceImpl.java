package pl.wk.teai_3.service;

import org.springframework.stereotype.Service;
import pl.wk.teai_3.model.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class CarServiceImpl implements ICarService {

    public CarServiceImpl(){
        carList.add(new Car(1, "Opel", "Astra", "GRAY"));
        carList.add(new Car(2, "Ford", "Ka", "GREEN"));
        carList.add(new Car(3, "Peugeot", "307", "WHITE"));
    }

    private List<Car> carList = new ArrayList<>();

    @Override
    public List<Car> getCars() {
        return carList;
    }

    @Override
    public int getCarIndex( long id  ) {
        List<Car> entries = getCars();
        int index = IntStream.range(0, entries.size())
            .filter(i -> id == entries.get(i).getId())
            .findFirst().orElse(-1);
        return index;
    }

    @Override
    public Car getCarById(long id) {
        Optional<Car> r = getCars()
                .stream()
                .filter(car -> car.getId() == id)
                .findFirst();
        return r.isPresent() ? r.get() : null;
    }

    @Override
    public List<Car> getCarsByColor(String color) {
        return getCars()
                .stream()
                .filter(car -> color.equalsIgnoreCase(car.getColor()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean addCar(Car car) {
        return carList.add(car);
    }

    @Override
    public boolean modifyCar( Car car, Map<String, String> requestParams ) {
//      clone car
        for( Map.Entry<String, String> entry : requestParams.entrySet() ) {
            switch( entry.getKey() ) {
                case "mark":
                    car.setMark(entry.getValue());
                    break;
                case "color":
                    car.setColor(entry.getValue());
                    break;
                case "model":
                    car.setModel(entry.getValue());
                    break;
                default:
                    // restore car
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean setCar( int index, Car car ) {
        Car oldCar = getCars().get(index);
        if( oldCar.getId() != car.getId() ) {
            return false;
        }
        getCars().set(index, car);
        return true;
    }

    @Override
    public boolean removeCar( int index, long id ) {
        Car oldCar = getCars().get(index);
        if( oldCar.getId() != id ) {
            return false;
        }
        getCars().remove(index);
        return true;
    }
}
