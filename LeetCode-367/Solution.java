// ============================================================================
// 力扣 367. 有效的完全平方数
// ----------------------------------------------------------------------------
// 题目描述
// 给你一个正整数 num。如果 num 是一个完全平方数，则返回 true，否则返回 false。
// 完全平方数 是一个可以写成某个整数的平方的整数。换句话说，它可以写成
// 某个整数和自身的乘积。
// 不能使用任何内置的库函数，如 sqrt。
//
// 函数签名（力扣已经给出，不用自己读输入，把方法补全提交即可）
//   public boolean isPerfectSquare(int num)
//
// 输入 #1
//   num = 16
// 输出 #1
//   true
//   解释：返回 true，因为 4 * 4 = 16 且 4 是一个整数。
//
// 输入 #2
//   num = 14
// 输出 #2
//   false
//   解释：返回 false，因为 3.742 * 3.742 = 14 但 3.742 不是一个整数。
//
// 提示
//   1 <= num <= 2^31 - 1
//
// 说明/提示
// 【样例说明】样例 1 里 16 是某个整数的平方（4 * 4），返回 true；
//   样例 2 里 14 的平方根 3.742... 不是整数，14 夹在 3 * 3 = 9 和
//   4 * 4 = 16 之间，返回 false。
//   所以这题等价于问："存不存在一个整数 mid，使得 mid * mid 恰好等于 num"。
// 【评测用例规模与约定】
//   num 最大到 2^31 - 1（int 上限 2147483647），平方根约 46340.95，
//   力扣难度：简单。二分每次砍一半，最多 O(log num) ≈ 31 次就够，
//   而且答案一定在 [1, 46340] 这个范围里（不会出现 num = 1 以外的负数）。
//
// 思路（二分查找之"精确找值"，和 LeetCode-374 一个框架）
//   把问题翻译成二分能回答的形式：
//     找整数 mid，使得 mid * mid == num。
//   mid 越大 mid * mid 越大，"mid * mid 与 num 比大小"这个比较是有序的，
//   所以可以在 [1, num]（甚至 [1, 46340]）上二分。
//
//   闭区间二分（left / right 都可能是答案），区间 [left, right] 初始 [1, num]：
//     int left = 1, right = num;
//     while (left <= right) {
//         int mid = left + (right - left) / 2;
//         long sq = (long) mid * mid;
//         if (sq == num) {
//             return true;          // 正好命中
//         } else if (sq < num) {
//             left = mid + 1;       // mid 太小，往大的一侧找
//         } else {
//             right = mid - 1;      // mid 太大，往小的一侧找
//         }
//     }
//     return false;                 // 区间找空还没命中，说明不是完全平方数
//
//   关键坑：
//     1. mid * mid 会溢出。num 能到 2^31 - 1，二分过程中 mid 可能取到
//        10 亿量级，mid * mid 直接超出 int 变负数，判断全错。
//        必须先转 long：写成 (long) mid * mid，或者像 69 那样
//        long left/right 再比。
//     2. mid 别写 (left + right) / 2：left + right 也可能溢出成负数，
//        导致死循环。写 left + (right - left) / 2。
//     3. 循环条件用 while (left <= right) 就必须配 left = mid + 1 /
//        right = mid - 1（mid 已经比过了，可以排除）；写成 right = mid
//        区间不缩小会死循环。
//
//   另一种写法（复用 69 的思路）：先二分求 floor(sqrt(num)) 记作 ans，
//   最后 return (long) ans * ans == num; 效果一样，代码多一步。
//
//   走一遍样例 2（num = 14）：区间 [1,14] → mid=7, 49>14 → right=6；
//   mid=3, 9<14 → left=4；mid=5, 25>14 → right=4；mid=4, 16>14 → right=3；
//   left=4 > right=3 结束，没命中，返回 false，和样例一致。
//   样例 1（num = 16）：mid 依次 8、4，第二轮 4*4=16 命中，返回 true。
//
//   边界：num = 1 时区间 [1,1]，mid = 1，1*1 = 1 命中返回 true（1 是完全
//   平方数）；num = 2 时 mid=1 太小 → left=2，mid=2, 4>2 → right=1，结束
//   返回 false。时间 O(log num)，额外空间 O(1)。
//
//   其他能过的解法（了解即可）：
//     - 完全平方数 = 前 n 个奇数和：1 + 3 + 5 + ... + (2n-1) = n^2。
//       用 num 不断减 1、3、5……，减到 0 就是完全平方数，减成负数就不是。
//       最坏约 46340 次，能过但不是最优。
//     - 牛顿迭代法：从 num 开始反复 x = (x + num / x) / 2 逼近整数平方根。
// ============================================================================

class Solution {
    public boolean isPerfectSquare(int num) {
        // 模板法来写
        // 定义左右边界
        long left = -1;
        long right = num;
        if(num == 1){
            return true;
        }
        while(left + 1 <right){
            long mid = left + (right - left) / 2;
            long ans = mid * mid;
            if(ans == num){
                return true;
            }else if(ans < num){
                left = mid;
            }else{
                right = mid;
            }
        }
        // 找完了都还没找到，说明不是完全平方数
        return false;
    }
}
