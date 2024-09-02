export interface Job {
    id: number;
    title: string;
    // Thêm các thuộc tính khác nếu có
  }
export interface Candidate {
    id: number;
    fullName: string;
    email: string;
    dob: string;
    phoneNumber: string;
    position: string;
    status: string;
    cvUrl: string;
    address: string;
    gender: string;
    skills: string[];
    note: string;
    yearOfExperience: number;
    recruiterId: number;
    recruiterName: string
    highestLevel: string;
    job: Job[];

}
export interface JobDetailsDTO {
  id: number;
  title: string;
  workingAddress : string;
  description: string;
  // Thêm các thuộc tính khác nếu có
}

