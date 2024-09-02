package com.example.ims_backend.service.impl;

import com.example.ims_backend.common.*;
import com.example.ims_backend.dto.CandidateDTO;
import com.example.ims_backend.dto.JobDTO;
import com.example.ims_backend.dto.PositionJobDTO;
import com.example.ims_backend.dto.UserDTO;
import com.example.ims_backend.dto.request.interview.DataSearch;
import com.example.ims_backend.dto.request.interview.InterviewAdd;
import com.example.ims_backend.dto.request.interview.InterviewRequest;
import com.example.ims_backend.dto.response.interview.*;
import com.example.ims_backend.entity.*;
import com.example.ims_backend.repository.CandidateRepository;
import com.example.ims_backend.repository.InterviewRepository;
import com.example.ims_backend.repository.JobRepository;
import com.example.ims_backend.repository.UserRepository;
import com.example.ims_backend.repository.specification.InterviewSpecification;
import com.example.ims_backend.repository.specification.OfferSpecification;
import com.example.ims_backend.service.EmailService;
import com.example.ims_backend.service.InterviewService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;

@Service
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class InterviewServiceImpl implements InterviewService {
    @Autowired
    private InterviewRepository interviewRepository;
    @Autowired
    private  CandidateRepository candidateRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    List<InterviewResponse> entityConvertRes(List<Interview> entities){
        List<InterviewResponse> interviewResponses = new ArrayList<>();

        for(Interview interview : entities) {
            InterviewResponse interviewResponse = new InterviewResponse();
            interviewResponse.setId(interview.getId());
            interviewResponse.setTitle(interview.getTitle());
            interviewResponse.setPositionLevel(String.valueOf(interview.getPositionLevel()));
            Candidate candidate = interview.getCandidate();
            interviewResponse.setCandidateName(candidate.getFullName());
            interviewResponse.setScheduleTime(interview.getInterviewDate().toString());
            interviewResponse.setFromTime(interview.getFromTime().toString());
            interviewResponse.setToTime(interview.getToTime().toString());
            interviewResponse.setResult(String.valueOf(interview.getResult()));
            interviewResponse.setStatus(String.valueOf(interview.getStatus()));
            Set<User> interviewers = interview.getInterviewers();
            Iterator<User> iterator = interviewers.iterator();
            List<UserDTO> interviewersList = new ArrayList<>();
            while (iterator.hasNext()){
                User user = iterator.next();
                interviewersList.add(new UserDTO(user.getId(),user.getFullName(),user.getEmail()));
            }
            interviewResponse.setInterviewers(interviewersList);
            interviewResponses.add(interviewResponse);
        }
        return interviewResponses;
    }


    @Override
    public Page<InterviewResponseDTO> getInterviews(Pageable pageable, String keyword, Long interviewer, InterviewStatus status) {
        log.info("Fetching offers with filters - keyword: {}, interviewer: {}, status: {}", keyword, interviewer,status);

        Specification<Interview> spec = Specification.where(InterviewSpecification.hasKeyword(keyword))
                .and(InterviewSpecification.hasStatus(status))
                .and(InterviewSpecification.hasInterviewer(interviewer));

        Page<Interview> interviews = interviewRepository.findAll(spec, pageable);
        List<InterviewResponseDTO> interviewResponsDTOS = interviews.stream().map(o ->{
            InterviewResponseDTO interviewResponseDTO = new InterviewResponseDTO();
            interviewResponseDTO.setId(o.getId());
            interviewResponseDTO.setTitle(o.getTitle());
            interviewResponseDTO.setPositionLevel(String.valueOf(o.getPositionLevel()));
            Candidate candidate = o.getCandidate();
            interviewResponseDTO.setCandidateName(candidate.getFullName());
            interviewResponseDTO.setScheduleTime(o.getInterviewDate().toString());
            interviewResponseDTO.setFromTime(o.getFromTime().toString());
            interviewResponseDTO.setToTime(o.getToTime().toString());
            interviewResponseDTO.setResult(String.valueOf(o.getResult()));
            interviewResponseDTO.setStatus(String.valueOf(o.getStatus()));
            interviewResponseDTO.setJob(new JobDTO(o.getJob().getId(),
                    o.getJob().getTitle(), o.getJob().getLastUpdatedBy().getFullName(),null));
            Set<User> interviewers = o.getInterviewers();
            Iterator<User> iterator = interviewers.iterator();
            List<UserDTO> interviewersList = new ArrayList<>();
            while (iterator.hasNext()){
                User user = iterator.next();
                interviewersList.add(new UserDTO(user.getId(),user.getFullName(),user.getEmail()));
            }
            interviewResponseDTO.setInterviewers(interviewersList);
            return interviewResponseDTO;
        }).toList();
        return new PageImpl<>(interviewResponsDTOS, pageable, interviews.getTotalElements());
    }


    @Override
    public InterviewDetailsResponse InterviewDetail(Long id) {
        try {
            Interview interview = interviewRepository.findById(id).get();
            InterviewDetailsResponse interViewDetailsResponse = new InterviewDetailsResponse();
            interViewDetailsResponse.setId(interview.getId());
            interViewDetailsResponse.setPositionLevel(interview.getJob().getTitle());
            interViewDetailsResponse.setTitle(interview.getTitle());
            interViewDetailsResponse.setCandidateName(interview.getCandidate().getFullName());
            interViewDetailsResponse.setScheduleTime(interview.getInterviewDate().toString());
            interViewDetailsResponse.setFromTime(interview.getFromTime().toString());
            interViewDetailsResponse.setToTime(interview.getToTime().toString());
            interViewDetailsResponse.setResult(String.valueOf(interview.getResult()));
            interViewDetailsResponse.setStatus(String.valueOf(interview.getStatus()));
            Set<User> interviewers = interview.getInterviewers();
            Iterator<User> iterator = interviewers.iterator();
            List<UserDTO> interviewersList = new ArrayList<>();
            while (iterator.hasNext()){
                User user = iterator.next();
                interviewersList.add(new UserDTO(user.getId(),user.getFullName(),user.getEmail()));
            }
            interViewDetailsResponse.setInterviewers(interviewersList);
            interViewDetailsResponse.setLocation(interview.getLocation());
            interViewDetailsResponse.setRecruiter(interview.getRecruiter().getFullName());
            interViewDetailsResponse.setMeetingId(interview.getMeetingId());
            interViewDetailsResponse.setNote(interview.getNote());
            return interViewDetailsResponse;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean deleteInterview(Long id) {
        try {
            interviewRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean addInterview(InterviewAdd interviewAdd) {
        try {
            Interview interview = new Interview();
            interview.setTitle(interviewAdd.getTitle());
            Candidate candidate = candidateRepository.findById(interviewAdd.getCandidate().get(0).getId()).get();
            interview.setCandidate(candidate);
            interview.setPositionLevel(PositionLevel.valueOf(interviewAdd.getJob().get(0).getPosition().get(0)));
            interview.setInterviewDate(LocalDate.parse(interviewAdd.getScheduleTime()));
            interview.setFromTime(LocalTime.parse(interviewAdd.getFromTime()));
            interview.setToTime(LocalTime.parse(interviewAdd.getToTime()));
            interview.setNote(interviewAdd.getNote());
            interview.setLocation(interviewAdd.getLocation());
            interview.setMeetingId(interviewAdd.getMeetingId());
            interview.setResult(InterviewResult.valueOf(interviewAdd.getResult()));
            interview.setStatus(InterviewStatus.valueOf(interviewAdd.getStatus()));
            interview.setRecruiter(jobRepository.findById(interviewAdd.getJob().get(0).getId()).get().getLastUpdatedBy());
            Set<User> interviewers = new HashSet<>();
            for(UserDTO user : interviewAdd.getInterviewers()){
                interviewers.add(userRepository.findById(user.getId()).get());
            }
            interview.setJob(jobRepository.findById(interviewAdd.getJob().get(0).getId()).get());
            interview.setInterviewers(interviewers);
            interviewRepository.save(interview);
            Email email = new Email();
            email.setEmail(candidate.getEmail());
            email.setSubject("Lịch phỏng vấn" + interview.getTitle());
            String body = "Dear " + candidate.getFullName() + ",\n" +
                    "Bạn có một cuộc phỏng vấn vào lúc " + interview.getFromTime() + " đến " + interview.getToTime() + " ngày " + interview.getInterviewDate() + " tại " + interview.getMeetingId()+ ".\n" +
                    "Vui lòng chuẩn bị kỹ lưỡng và đến đúng giờ.\n" +
                    "Trân trọng!";
            email.setBody(body);
            emailService.sendEmail(email);
            for (User user : interview.getInterviewers()) {
                Email email1 = new Email();
                email1.setEmail(user.getEmail());
                email1.setSubject("Lịch phỏng vấn" + interview.getTitle());
                String body1 = "Dear " + user.getFullName() + ",\n" +
                        "Bạn có một cuộc phỏng vấn vào lúc " + interview.getFromTime() + " đến " + interview.getToTime() + " ngày " + interview.getInterviewDate() + " tại " + interview.getMeetingId() + ".\n" +
                        "Vui lòng chuẩn bị kỹ lưỡng và đến đúng giờ.\n" +
                        "Trân trọng!";
                email1.setBody(body1);
                emailService.sendEmail(email1);
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean updateInterview(Long id, InterviewRequest interviewRequest) {
        try {
            Interview interview = interviewRepository.findById(id).get();
            interview.setTitle(interviewRequest.getTitle());
            Candidate candidate = candidateRepository.findById(interviewRequest.getCandidate().get(0).getId()).get();
            interview.setCandidate(candidate);
            interview.setPositionLevel(PositionLevel.valueOf(interviewRequest.getJob().get(0).getPosition().get(0)));
            interview.setInterviewDate(LocalDate.parse(interviewRequest.getScheduleTime()));
            interview.setFromTime(LocalTime.parse(interviewRequest.getFromTime()));
            interview.setToTime(LocalTime.parse(interviewRequest.getToTime()));
            interview.setNote(interviewRequest.getNote());
            interview.setLocation(interviewRequest.getLocation());
            interview.setMeetingId(interviewRequest.getMeetingId());
            interview.setResult(InterviewResult.valueOf(interviewRequest.getResult()));
            interview.setStatus(InterviewStatus.valueOf(interviewRequest.getStatus()));
            interview.setRecruiter(jobRepository.findById(interviewRequest.getJob().get(0).getId()).get().getLastUpdatedBy());
            Set<User> interviewers = new HashSet<>();
            for(UserDTO user : interviewRequest.getInterviewers()){
                interviewers.add(userRepository.findById(user.getId()).get());
            }
            interview.setJob(jobRepository.findById(interviewRequest.getJob().get(0).getId()).get());
            interview.setInterviewers(interviewers);
            interviewRepository.save(interview);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public InterviewShowUp InterviewShowUp(Long id) {
        Interview interview = interviewRepository.findById(id).get();
        InterviewShowUp interviewShowUp = new InterviewShowUp();
        interviewShowUp.setId(interview.getId());
        interviewShowUp.setTitle(interview.getTitle());
        interviewShowUp.setPositionLevel(String.valueOf(interview.getPositionLevel()));
        interviewShowUp.setCandidateName(interview.getCandidate().getFullName());
        interviewShowUp.setScheduleTime(interview.getInterviewDate().toString());
        interviewShowUp.setFromTime(interview.getFromTime().toString());
        interviewShowUp.setToTime(interview.getToTime().toString());
        interviewShowUp.setResult(String.valueOf(interview.getResult()));
        interviewShowUp.setStatus(String.valueOf(interview.getStatus()));
        interviewShowUp.setJobTitle(interview.getJob().getTitle());
        Set<User> interviewers = interview.getInterviewers();
        Iterator<User> iterator = interviewers.iterator();
        List<UserDTO> interviewersList = new ArrayList<>();
        while (iterator.hasNext()){
            User user = iterator.next();
            interviewersList.add(new UserDTO(user.getId(),user.getFullName(),user.getEmail()));
        }
        interviewShowUp.setInterviewers(interviewersList);
        interviewShowUp.setLocation(interview.getLocation());
        interviewShowUp.setRecruiter(interview.getRecruiter().getFullName());
        interviewShowUp.setMeetingId(interview.getMeetingId());
        interviewShowUp.setNote(interview.getNote());
        List <Job> jobs = jobRepository.findAll();
        List <JobDTO> jobDTOs = new ArrayList<>();

        for( Job job : jobs){
            JobDTO jobDTO = new JobDTO();
            jobDTO.setId(job.getId());
            jobDTO.setRecruiter(job.getLastUpdatedBy().getFullName());
            Set<PositionLevel> positionLevels = job.getLevels();
            List<String> position = new ArrayList<>();
            for(PositionLevel positionLevel : positionLevels){
                position.add(String.valueOf(positionLevel));
            }
            jobDTO.setPosition(position);
            jobDTOs.add(jobDTO);
        }
        List<Candidate> candidates = candidateRepository.findAll();
        List<CandidateDTO> candidateDTOs = new ArrayList<>();
        for (Candidate candidate : candidates) {
            Set<Job> jobList = candidate.getJobs();
            List<PositionJobDTO> positionJobDTOS = new ArrayList<>();
            for( Job job : jobList){
                PositionJobDTO positionJobDTO = new PositionJobDTO();
                positionJobDTO.setIdJob(job.getId());
                positionJobDTO.setTitle(job.getTitle());
                positionJobDTOS.add(positionJobDTO);
            }
            candidateDTOs.add(new CandidateDTO(candidate.getId(), candidate.getFullName(), candidate.getEmail(), positionJobDTOS));
        }
        List<User> users = userRepository.findByRole(Role.valueOf("ROLE_INTERVIEWER"));
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users){
            userDTOs.add(new UserDTO(user.getId(),user.getFullName(),user.getEmail()));
        }
        interviewShowUp.setJob(jobDTOs);
        interviewShowUp.setCandidate(candidateDTOs);
        interviewShowUp.setUserinterviewers(userDTOs);
        return interviewShowUp;
    }

    @Override
    public InterviewShowAdd InterviewShowAdd() {
        InterviewShowAdd interviewShowAdd = new InterviewShowAdd();
        List <Job> jobs = jobRepository.findAll();
        List <JobDTO> jobDTOs = new ArrayList<>();

        for(Job job : jobs){
            JobDTO jobDTO = new JobDTO();
            jobDTO.setId(job.getId());
            jobDTO.setRecruiter(job.getLastUpdatedBy().getFullName());
            Set<PositionLevel> positionLevels = job.getLevels();
            List<String> position = new ArrayList<>();
            for(PositionLevel positionLevel : positionLevels){
                position.add(String.valueOf(positionLevel));
            }
            jobDTO.setPosition(position);
            jobDTOs.add(jobDTO);
        }
        List<Candidate> candidates = candidateRepository.findAll();
        List<CandidateDTO> candidateDTOs = new ArrayList<>();
        for (Candidate candidate : candidates) {
            Set<Job> jobList = candidate.getJobs();
            List<PositionJobDTO> positionJobDTOS = new ArrayList<>();
            for(Job job : jobList){
                PositionJobDTO positionJobDTO = new PositionJobDTO();
                positionJobDTO.setIdJob(job.getId());
                positionJobDTO.setTitle(job.getTitle());
                positionJobDTOS.add(positionJobDTO);
            }
            candidateDTOs.add(new CandidateDTO(candidate.getId(), candidate.getFullName(), candidate.getEmail(), positionJobDTOS));
        }
        List<User> users = userRepository.findByRole(Role.valueOf("ROLE_INTERVIEWER"));
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users){
            userDTOs.add(new UserDTO(user.getId(),user.getFullName(),user.getEmail()));
        }
        interviewShowAdd.setJob(jobDTOs);
        interviewShowAdd.setCandidate(candidateDTOs);
        interviewShowAdd.setUserinterviewers(userDTOs);
        return interviewShowAdd;
    }

    @Override
    public boolean sendEmail(Long id) throws MessagingException {
        Interview interview = interviewRepository.findById(id).get();
        Candidate candidate = interview.getCandidate();
         try{
            Email email = new Email();
            email.setEmail(candidate.getEmail());
            email.setSubject("Lịch phỏng vấn" + interview.getTitle());
            String body = "Dear " + candidate.getFullName() + ",\n" +
                    "Bạn có một cuộc phỏng vấn vào lúc " + interview.getFromTime() + " đến " + interview.getToTime() + " ngày " + interview.getInterviewDate() + " tại " + interview.getMeetingId()+ ".\n" +
                    "Vui lòng chuẩn bị kỹ lưỡng và đến đúng giờ.\n" +
                    "Trân trọng!";
            email.setBody(body);
            emailService.sendEmail(email);
            for (User user : interview.getInterviewers()){
                Email email1 = new Email();
                email1.setEmail(user.getEmail());
                email1.setSubject("Lịch phỏng vấn" + interview.getTitle());
                String body1 = "Dear " + user.getFullName() + ",\n" +
                        "Bạn có một cuộc phỏng vấn vào lúc " + interview.getFromTime() + " đến " + interview.getToTime() + " ngày " + interview.getInterviewDate() + " tại " + interview.getMeetingId()+ ".\n" +
                        "Vui lòng chuẩn bị kỹ lưỡng và đến đúng giờ.\n" +
                        "Trân trọng!";
                email1.setBody(body1);
                emailService.sendEmail(email1);
            }
            return true;
        } catch (Exception e) {
             return false;
         }
    }

    @Override
    public List<InterviewResponseDTO> candidateInterviews(Long id) {
        List<Interview> interviews = interviewRepository.findByCandidateId(id);
        return interviews.stream().map(entity -> {
            InterviewResponseDTO interviewResponseDTO = new InterviewResponseDTO();
            interviewResponseDTO.setId(entity.getId());
            interviewResponseDTO.setTitle(entity.getTitle());
            Candidate candidate = entity.getCandidate();
            interviewResponseDTO.setScheduleTime(entity.getInterviewDate().toString());
            interviewResponseDTO.setFromTime(entity.getFromTime().toString());
            interviewResponseDTO.setToTime(entity.getToTime().toString());
            interviewResponseDTO.setResult(entity.getResult().toString());
            interviewResponseDTO.setStatus(entity.getStatus().toString());
            Set<User> interviewers = entity.getInterviewers();
            Iterator<User> iterator = interviewers.iterator();
            List<UserDTO> interviewersList = new ArrayList<>();
            while (iterator.hasNext()) {
                User user = iterator.next();
                interviewersList.add(new UserDTO(user.getId(), user.getFullName(), user.getEmail()));
            }
            interviewResponseDTO.setInterviewers(interviewersList);
            interviewResponseDTO.setLocation(entity.getLocation());
            interviewResponseDTO.setRecruiter(new UserDTO(entity.getRecruiter().getId(),
                    entity.getRecruiter().getFullName(), entity.getRecruiter().getEmail()));
            interviewResponseDTO.setMeetingId(entity.getMeetingId());
            interviewResponseDTO.setNote(entity.getNote());
            Set<PositionLevel> levels = entity.getJob().getLevels();
            Iterator<PositionLevel> levelIterator = levels.iterator();
            List<String> levelList = new ArrayList<>();
            while (levelIterator.hasNext()) {
                levelList.add(levelIterator.next().name());
            }
            interviewResponseDTO.setJob(new JobDTO(entity.getJob().getId(), entity.getJob().getTitle(),
                    entity.getJob().getLastUpdatedBy().getFullName(), levelList));
            return interviewResponseDTO;
        }).toList();
    }

    @Override
    public boolean send(Long id, String node, InterviewResult result) {
        Interview interview = interviewRepository.findById(id).get();
        Candidate candidate= interview.getCandidate();
        interview.setResult(result);
        interview.setNote(node);
        interview.setStatus(InterviewStatus.INTERVIEWED);
        interviewRepository.save(interview);
        if(interview.getResult().equals(InterviewResult.PASSED)){
            Email email = new Email();
            email.setEmail(candidate.getEmail());
            email.setSubject("Kết quả phỏng vấn");
            String body = "Dear " + candidate.getFullName() + ",\n" +
                    "Chúc mừng bạn đã vượt qua vòng phỏng vấn của chúng tôi.\n" +
                    "Chúng tôi có 1 số nhận xét như sau: \n" + node + "\n" +
                    "Chúng tôi sẽ liên hệ với bạn để thống nhất về việc làm.\n" +
                    "Trân trọng!";
            email.setBody(body);
            emailService.sendEmail(email);
            return true;
        }else if(interview.getResult().equals(InterviewResult.FAILED)){
            Email email = new Email();
            email.setEmail(candidate.getEmail());
            email.setSubject("Kết quả phỏng vấn");
            String body = "Dear " + candidate.getFullName() + ",\n" +
                    "Rất tiếc bạn đã không vượt qua vòng phỏng vấn của chúng tôi.\n" +
                    "Chúng tôi có 1 số nhận xét như sau: \n" + node + "\n" +
                    "Mong sẽ được làm việc với bạn ở tương lai không xa .\n" +
                    "Trân trọng!";
            email.setBody(body);
            emailService.sendEmail(email);
            return true;
        }

        return false;

    }

}
