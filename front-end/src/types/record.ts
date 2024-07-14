 
export interface Record {
    money:number;
    tag:number;
    date:string;
    remark:string;
}
export interface output_Record {
    money:number;
    tag:string;
    date:string;
    remark:string;
}
export const RecordTag = [
    "无",
    "收入",
    "穿着",
    "饮食",
    "交通",
    "住宿",
    "娱乐",
    "医疗",
    "其他"
]

export const formatDate = (date: Date) => {
    const adjustedDate = new Date(date.getTime() + 8 * 60 * 60 * 1000);
    return adjustedDate.toISOString().slice(0, 10).replace(/-/g, '');
}

export interface record_response 
{date: string;
income: boolean;
money: number;
record_id: number;
remark: string;
tag: number;
user_id: number;
}
// export interface SelRecordResponse {date: string;
// income: string;
// money: number;
// remark: string;
// tag: string;
// user_id: number;
// }

export const convertToRecordArray = (responseArray: record_response[]): output_Record[] => {
    return responseArray.map((record): output_Record => ({
        money: record.money/100,
        tag: RecordTag[record.tag],
        date: record.date,
        remark: record.remark
    }));
};

export const convertToUploadeRecordArray = (responseArray: any[]): Record[] => {
    return responseArray.map((record): Record => ({
        money: record.money*100,
        date: record.date.replace(/-/g, ''),
        tag: RecordTag.indexOf(record.tag),
        remark: record.remark
    }))
}