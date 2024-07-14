import { describe, it, expect } from 'vitest';
import { convertToRecordArray, convertToUploadeRecordArray } from '@/types/record' // 替换为实际路径

describe('convertToRecordArray', () => {
  it('正确转换导出记录数组', () => {
    const responseArray = [
      { money: 10000, tag: 3, date: '2023-04-01', remark: 'Lunch' ,income:true, record_id:0, user_id:0},
      { money: 20000, tag: 4, date: '2023-04-02', remark: 'Taxi'  ,income:true, record_id:0, user_id:0}
    ];

    const expectedOutput = [
      { money: 100, tag: '饮食', date: '2023-04-01', remark: 'Lunch' },
      { money: 200, tag: '交通', date: '2023-04-02', remark: 'Taxi' }
    ];
    expect(convertToRecordArray(responseArray)).toEqual(expectedOutput);
  });
});

describe('convertToUploadeRecordArray', () => {
  it('正确转换上传记录数组', () => {
    const responseArray = [
      { money: 100, tag: '穿着', date: '2023-04-01', remark: 'Lunch' },
      { money: 200, tag: '收入', date: '2023-04-02', remark: '' }
    ];

    const expectedOutput = [
      { money: 10000, date: '20230401', tag: 2, remark: 'Lunch' },
      { money: 20000, date: '20230402', tag: 1, remark: '' }
    ];
    expect(convertToUploadeRecordArray(responseArray)).toEqual(expectedOutput);
  });
});
