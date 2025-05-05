package io.github.teamomo.moment.service;

import io.github.teamomo.moment.entity.Moment;
import io.github.teamomo.moment.repository.MomentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MomentService {
  private final MomentRepository momentRepository;

   public List<Moment> getAllMoments() {
       return momentRepository.findAll();
   }
}