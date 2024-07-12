 
export interface Record {
    money:number;
    tag:number;
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
    return date.toISOString().slice(0, 10).replace(/-/g, '');
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
export interface SelRecordResponse {date: string;
income: string;
money: number;
remark: string;
tag: string;
user_id: number;
}
