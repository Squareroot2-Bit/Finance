import {RecordTag} from "@/types/record"
export const generateDaysArray = (days: number): string[] => {
    if (days < 1 || days > 31) {
        throw new Error("天数必须在1到31之间");
    }

    const daysArray: string[] = [];
    for (let i = 1; i <= days; i++) {
        daysArray.push(i.toString().padStart(2, '0'));
    }

    return daysArray;
}
export interface pieIn{
    value: number;
    tag:number;
}

export interface pieOut{
    name: string;
    value: number;
}


export const generatePieData = (data: pieIn[]): pieOut[] => {
    return data.map(item => {
        return {
            name: RecordTag[item.tag],
            value: item.value/100
        }})};