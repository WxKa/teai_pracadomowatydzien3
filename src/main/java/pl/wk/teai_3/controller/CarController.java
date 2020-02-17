package pl.wk.teai_3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.wk.teai_3.model.Car;
import pl.wk.teai_3.service.ICarService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

@RestController
@RequestMapping(value = "/cars")
public class CarController {

    private ICarService carService;

    @Autowired
    public CarController( ICarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<List<Car>> getCars(){
        synchronized( carService ) {
            List<Car> cars = carService.getCars();
            return new ResponseEntity<>(cars, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable long id){
        synchronized( carService ) {
            Car carById = carService.getCarById(id);
            if( carById != null ) {
                return new ResponseEntity<>(carById, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }

    @GetMapping("/color/{color}")
    public ResponseEntity<List<Car>> getCarByColor(@PathVariable String color){
        synchronized( carService ) {
            List<Car> carList = carService.getCarsByColor(color);
            return new ResponseEntity<>(carList, HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity<Car> addCar(@RequestBody Car car){
        boolean add = carService.addCar(car);
        if(add){
            return new ResponseEntity(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping
    public ResponseEntity<Car> modCar(@RequestBody Car newCar){
        synchronized( carService ) {
            int index = carService.getCarIndex(newCar.getId());
            if( index >= 0 ) {
                carService.getCars().set(index, newCar);
                return new ResponseEntity<>(newCar, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<Car> patchCar(@PathVariable long id, @RequestParam Map<String, String> requestParams, Model model){
        synchronized( carService ) {
            Optional<Car> first = carService.getCars().stream().filter(car -> car.getId() == id).findFirst();
            if( first.isPresent() ) {
                if( carService.modifyCar(first.get(), requestParams) ) {
                    return new ResponseEntity<>(first.get(), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Car> removeCar(@PathVariable long id){
        synchronized( carService ) {
            int index = carService.getCarIndex(id);
            if( index >= 0 ) {
                Car oldCar = carService.getCars().get(index);
                if( carService.removeCar(index, id) ) {
                    return new ResponseEntity<>(oldCar, HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }
}
