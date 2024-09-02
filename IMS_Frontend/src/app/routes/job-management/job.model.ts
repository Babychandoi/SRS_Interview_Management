// job.model.ts
export interface Job {
    id: number;
    title: string;
    skills: string[];
    startDate: Date;
    endDate: Date;
    salaryFrom: number;
    salaryTo: number;
    workingAddress: string;
    benefits: string[];
    level: string;
    description: string;
    status: 'DRAFT' | 'OPEN' | 'CLOSE';
    createdDate: Date;
    lastUpdatedBy: string;
    lastUpdatedOn: Date;
}
